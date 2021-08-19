//import model.parsers.standard.Computer;
//import model.parsers.standard.Loader;
//import model.parsers.standard.StandardParser;
//import model.parsers.standard.State;
//import model.variables.MatrixVariable;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class StandardParserTest {
//    Loader loader = new Loader();
//    Computer computer = new Computer();
//
//    @Test
//    void removeWhitespaceAndHashtags() {
//        State state = loader.load("#a#a#=#1#;b    b   =   1   ;\nc\nc\n=\n1\n;\rd\r\rd\r=\r1\r; e e = 1 ", "");
//        assertTrue(state.varBoxes.containsKey("aa"));
//        assertTrue(state.varBoxes.containsKey("bb"));
//        assertTrue(state.varBoxes.containsKey("cc"));
//        assertTrue(state.varBoxes.containsKey("dd"));
//        assertTrue(state.varBoxes.containsKey("ee"));
//        assertEquals(state.varBoxes.size(), 5);
//    }
//
//    @Test
//    void parseVariableDefinitionStructure() {
//        State state;
//        //returns message on throw (to parser to print status for user)
//        state = loader.load("a;1", "");
//        assertNotNull(state.msg);
//        state = loader.load("0a=1", "");
//        assertNotNull(state.msg);
//        state = loader.load("a=\"", "");
//        assertNotNull(state.msg);
//        state = loader.load("a=(", "");
//        assertNotNull(state.msg);
//        state = loader.load("a=[", "");
//        assertNotNull(state.msg);
//        state = loader.load("a=aaa", "");
//        assertNotNull(state.msg);
//        state = loader.load("a=1..1", "");
//        assertNotNull(state.msg);
//        state = loader.load("a=--1.1", "");
//        assertNotNull(state.msg);
//        state = loader.load("a=[1,1/1]", "");
//        assertNotNull(state.msg);
//        state = loader.load("a=[1..1]", "");
//        assertNotNull(state.msg);
//        state = loader.load("a=[--1.1]", "");
//        assertNotNull(state.msg);
//        state = loader.load("a=[]", "");
//        assertNotNull(state.msg);
//        state = loader.load("a=[/]", "");
//        assertNotNull(state.msg);
//    }
//
//    @Test
//    void correctVariablesPass() {
//        final State[] state = new State[1];//cause lambda
//        assertDoesNotThrow(() -> state[0] = loader.load("a=1;b=-1;c=1.1;d=-1.1;e=\"\";f=\"\"\";g=(1);h=(1,-1,1.1,-1.1);i=[1];j=[1/-1/1.1/-1.1];k=[1,-1/1.1,-1.1];l=[1,-1,1.1,-1.1]", ""));
//        assertEquals(state[0].varBoxes.size(), 12);
//        assertEquals(state[0].varBoxes.get("a").getValue(), 1d);
//        assertEquals(state[0].varBoxes.get("b").getValue(), -1d);
//        assertEquals(state[0].varBoxes.get("c").getValue(), 1.1d);
//        assertEquals(state[0].varBoxes.get("d").getValue(), -1.1d);
//        assertEquals(state[0].varBoxes.get("e").getValue(), (""));
//        assertEquals(state[0].varBoxes.get("f").getValue(), ("\""));
//        assertEquals(state[0].varBoxes.get("g").getValue(), ("(1)"));
//        assertEquals(state[0].varBoxes.get("h").getValue(), ("(1,-1,1.1,-1.1)"));
//        assertEquals(state[0].varBoxes.get("i"), new MatrixVariable(new double[][]{{1}}));
//        assertEquals(state[0].varBoxes.get("j"), new MatrixVariable(new double[][]{{1}, {-1}, {1.1}, {-1.1}}));
//        assertEquals(state[0].varBoxes.get("k"), new MatrixVariable(new double[][]{{1, -1}, {1.1, -1.1}}));
//        assertEquals(state[0].varBoxes.get("l"), new MatrixVariable(new double[][]{{1, -1, 1.1, -1.1}}));
//    }
//
//    @Test
//    void replaceConstants() {
//        final State[] state = new State[1];
//        assertDoesNotThrow(() -> state[0] = loader.load("", "1,1.1,-1,-1.1,--1,---1,--1.1,---1.1"));
//        assertEquals(state[0].varBoxes.size(), 4);
//        System.out.println(state[0].varBoxes.keySet());
//        assertEquals(state[0].varBoxes.get("01d0").getValue(), 1d);
//        assertEquals(state[0].varBoxes.get("0m1d0").getValue(), -1d);
//        assertEquals(state[0].varBoxes.get("01d1").getValue(), 1.1d);
//        assertEquals(state[0].varBoxes.get("0m1d1").getValue(), -1.1d);
//        assertDoesNotThrow(() -> state[0] = loader.load("", "-,--,---,-1..1"));
//        assertEquals(state[0].varBoxes.size(), 2);
//        assertEquals(state[0].varBoxes.get("01d0").getValue(), 1d);
//        assertEquals(state[0].varBoxes.get("0m1d0").getValue(), -1d);
//    }
//
//    @Test
//    void operationsAreSimplifiedCorrectly() {//input is changed into list of model.variables in postorder with recipes
//        final State[] state = new State[1];
//        assertDoesNotThrow(() -> state[0] = loader.load("a=1", "+(+(+(a),a),a,+(a,+(a)))"));
//        assertDoesNotThrow(() -> computer.compute(state[0]));
//        assertEquals(state[0].varBoxes.size(), 1 + state[0].futureVariables.size());
//        assertEquals(state[0].futureVariables.get("4var").first, "+");
//        assertEquals(state[0].futureVariables.get("4var").second.size(), 3);
//        assertEquals(state[0].futureVariables.get("4var").second.get(0), "1var");
//        assertEquals(state[0].futureVariables.get("4var").second.get(1), "a");
//        assertEquals(state[0].futureVariables.get("4var").second.get(2), "3var");
//        assertEquals(state[0].varBoxes.get("4var").getValue(), 5d);
//        assertEquals(state[0].futureVariables.get("3var").first, "+");
//        assertEquals(state[0].futureVariables.get("3var").second.size(), 2);
//        assertEquals(state[0].futureVariables.get("3var").second.get(0), "a");
//        assertEquals(state[0].futureVariables.get("3var").second.get(1), "2var");
//        assertEquals(state[0].varBoxes.get("3var").getValue(), 2d);
//        assertEquals(state[0].futureVariables.get("2var").first, "+");
//        assertEquals(state[0].futureVariables.get("2var").second.size(), 1);
//        assertEquals(state[0].futureVariables.get("2var").second.get(0), "a");
//        assertEquals(state[0].varBoxes.get("2var").getValue(), 1d);
//        assertEquals(state[0].futureVariables.get("1var").first, "+");
//        assertEquals(state[0].futureVariables.get("1var").second.size(), 2);
//        assertEquals(state[0].futureVariables.get("1var").second.get(0), "0var");
//        assertEquals(state[0].futureVariables.get("1var").second.get(1), "a");
//        assertEquals(state[0].varBoxes.get("1var").getValue(), 2d);
//        assertEquals(state[0].futureVariables.get("0var").first, "+");
//        assertEquals(state[0].futureVariables.get("0var").second.size(), 1);
//        assertEquals(state[0].futureVariables.get("0var").second.get(0), "a");
//        assertEquals(state[0].varBoxes.get("0var").getValue(), 1d);
//        assertEquals(state[0].varBoxes.get("a").getValue(), 1d);
//    }
//    //@Test
//    //void testify() {
//    //    State state=loader.load("","*--+--+--++++--++1.00--3.4");
//    //    System.out.println(state.varBoxes);
//    //}
//}
