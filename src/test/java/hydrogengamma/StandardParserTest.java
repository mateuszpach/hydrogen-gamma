package hydrogengamma;

import hydrogengamma.model.parsers.standard.*;
import hydrogengamma.model.variables.MatrixVariable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StandardParserTest {
    Loader loader = new Loader();
    Computer computer = new Computer();

    @Test
    void removeWhitespaceAndHashtags() {
        State state = loader.load("#a#a#=#1#;b    b   =   1   ;\nc\nc\n=\n1\n;\rd\r\rd\r=\r1\r; e e = 1 ", "");
        assertTrue(state.containsKey("aa"));
        assertTrue(state.containsKey("bb"));
        assertTrue(state.containsKey("cc"));
        assertTrue(state.containsKey("dd"));
        assertTrue(state.containsKey("ee"));
        assertEquals(state.expressions.size() - state.getComputationOrder().size(), 5);
    }

    @Test
    void parseVariableDefinitionStructure() {
        State state;
        //returns message on throw (to parser to print status for user)
        assertThrows(ParsingException.class, () -> loader.load("a;1", ""));
        assertThrows(ParsingException.class, () -> loader.load("0a=1", ""));
        assertThrows(ParsingException.class, () -> loader.load("a=\"", ""));
        assertThrows(ParsingException.class, () -> loader.load("a=(", ""));
        assertThrows(ParsingException.class, () -> loader.load("a=[", ""));
        assertThrows(ParsingException.class, () -> loader.load("a=aaa", ""));
        assertThrows(ParsingException.class, () -> loader.load("a=1..1", ""));
        assertThrows(ParsingException.class, () -> loader.load("a=--1.1", ""));
        assertThrows(ParsingException.class, () -> loader.load("a=[1,1/1]", ""));
        assertThrows(ParsingException.class, () -> loader.load("a=[1..1]", ""));
        assertThrows(ParsingException.class, () -> loader.load("a=[--1.1]", ""));
        assertThrows(ParsingException.class, () -> loader.load("a=[]", ""));
        assertThrows(ParsingException.class, () -> loader.load("a=[/]", ""));
    }

    @Test
    void correctVariablesPass() {
        final State[] state = new State[1];//cause lambda
        assertDoesNotThrow(() -> state[0] = loader.load("a=1;b=-1;c=1.1;d=-1.1;e=\"\";f=\"\"\";g=(1);h=(1,-1,1.1,-1.1);i=[1];j=[1/-1/1.1/-1.1];k=[1,-1/1.1,-1.1];l=[1,-1,1.1,-1.1]", ""));
        assertEquals(state[0].expressions.size() - state[0].getComputationOrder().size(), 12);
        assertEquals(state[0].expressions.get("a").getVariable().getValue(), 1d);
        assertEquals(state[0].expressions.get("b").getVariable().getValue(), -1d);
        assertEquals(state[0].expressions.get("c").getVariable().getValue(), 1.1d);
        assertEquals(state[0].expressions.get("d").getVariable().getValue(), -1.1d);
        assertEquals(state[0].expressions.get("e").getVariable().getValue(), (""));
        assertEquals(state[0].expressions.get("f").getVariable().getValue(), ("\""));
        assertEquals(state[0].expressions.get("g").getVariable().getValue(), ("(1)"));
        assertEquals(state[0].expressions.get("h").getVariable().getValue(), ("(1,-1,1.1,-1.1)"));
        assertEquals(state[0].expressions.get("i").getVariable(), new MatrixVariable(new double[][]{{1}}));
        assertEquals(state[0].expressions.get("j").getVariable(), new MatrixVariable(new double[][]{{1}, {-1}, {1.1}, {-1.1}}));
        assertEquals(state[0].expressions.get("k").getVariable(), new MatrixVariable(new double[][]{{1, -1}, {1.1, -1.1}}));
        assertEquals(state[0].expressions.get("l").getVariable(), new MatrixVariable(new double[][]{{1, -1, 1.1, -1.1}}));
    }

    @Test
    void replaceConstants() {
        final State[] state = new State[1];
        assertDoesNotThrow(() -> state[0] = loader.load("", "(-1)+(-1.1)+1.1+-1+-1.1+--1+---1+--1.1+---1.1"));
        assertEquals(state[0].expressions.size() - state[0].getComputationOrder().size(), 4);
        assertEquals(state[0].expressions.get("01d0").getVariable().getValue(), 1d);
        assertEquals(state[0].expressions.get("0m1d0").getVariable().getValue(), -1d);
        assertEquals(state[0].expressions.get("01d1").getVariable().getValue(), 1.1d);
        assertEquals(state[0].expressions.get("0m1d1").getVariable().getValue(), -1.1d);
        assertDoesNotThrow(() -> state[0] = loader.load("", "-+--+---+-1.1"));
        assertEquals(state[0].expressions.size() - state[0].getComputationOrder().size(), 1);
        assertEquals(state[0].expressions.get("0m1d1").getVariable().getValue(), -1.1d);
    }

    @Test
    void operationsAreSimplifiedCorrectly() {//input is changed into list of hydrogengamma.model.variables in postorder with recipes
        //final State[] state = new State[1];
        //assertDoesNotThrow(() -> state[0] = loader.load("a=1", "+(+(+(a),a),a,+(a,+(a)))"));
        //assertDoesNotThrow(() -> computer.compute(state[0]));
        //assertEquals(state[0].expressions.size(), 1 + state[0].expressions.size());
        //assertEquals(state[0].expressions.get("4var").getFormula().first, "+");
        //assertEquals(state[0].expressions.get("4var").getFormula().second.size(), 3);
        //assertEquals(state[0].expressions.get("4var").getFormula().second.get(0), "1var");
        //assertEquals(state[0].expressions.get("4var").getFormula().second.get(1), "a");
        //assertEquals(state[0].expressions.get("4var").getFormula().second.get(2), "3var");
        //assertEquals(state[0].expressions.get("4var").getVariable().getValue(), 5d);
        //assertEquals(state[0].expressions.get("3var").getFormula().first, "+");
        //assertEquals(state[0].expressions.get("3var").getFormula().second.size(), 2);
        //assertEquals(state[0].expressions.get("3var").getFormula().second.get(0), "a");
        //assertEquals(state[0].expressions.get("3var").getFormula().second.get(1), "2var");
        //assertEquals(state[0].expressions.get("3var").getVariable().getValue(), 2d);
        //assertEquals(state[0].expressions.get("2var").getFormula().first, "+");
        //assertEquals(state[0].expressions.get("2var").getFormula().second.size(), 1);
        //assertEquals(state[0].expressions.get("2var").getFormula().second.get(0), "a");
        //assertEquals(state[0].expressions.get("2var").getVariable().getValue(), 1d);
        //assertEquals(state[0].expressions.get("1var").getFormula().first, "+");
        //assertEquals(state[0].expressions.get("1var").getFormula().second.size(), 2);
        //assertEquals(state[0].expressions.get("1var").getFormula().second.get(0), "0var");
        //assertEquals(state[0].expressions.get("1var").getFormula().second.get(1), "a");
        //assertEquals(state[0].expressions.get("1var").getVariable().getValue(), 2d);
        //assertEquals(state[0].expressions.get("0var").getFormula().first, "+");
        //assertEquals(state[0].expressions.get("0var").getFormula().second.size(), 1);
        //assertEquals(state[0].expressions.get("0var").getFormula().second.get(0), "a");
        //assertEquals(state[0].expressions.get("0var").getVariable().getValue(), 1d);
        //assertEquals(state[0].expressions.get("a").getVariable().getValue(), 1d);
    }
/*
    //@Test
    //void testify() {
    //    State state=loader.load("","*--+--+--++++--++1.00--3.4");
    //    System.out.println(state.expressions);
    //}
    */
}
