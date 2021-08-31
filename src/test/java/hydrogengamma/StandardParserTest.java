package hydrogengamma;

import hydrogengamma.model.Variable;
import hydrogengamma.model.parsers.standard.ParsingException;
import hydrogengamma.model.parsers.standard.StandardComputer;
import hydrogengamma.model.parsers.standard.StandardLoader;
import hydrogengamma.model.variables.MatrixVariable;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class StandardParserTest {
    StandardLoader standardLoader = new StandardLoader();
    StandardComputer standardComputer = new StandardComputer();

    @Test
    void removeWhitespaceAndHashtags() {
        Map<String, Variable<?>> state = standardLoader.load("#a#a#=#1#;b    b   =   1   ;\nc\nc\n=\n1\n;\rd\r\rd\r=\r1\r; e e = 1 ");
        assertTrue(state.containsKey("aa"));
        assertTrue(state.containsKey("bb"));
        assertTrue(state.containsKey("cc"));
        assertTrue(state.containsKey("dd"));
        assertTrue(state.containsKey("ee"));
        assertEquals(state.size(), 5);
    }

    @Test
    void parseVariableDefinitionStructure() {
        //returns message on throw (to parser to print status for user)
        assertThrows(ParsingException.class, () -> standardLoader.load("a;1"));
        assertThrows(ParsingException.class, () -> standardLoader.load("0a=1"));
        assertThrows(ParsingException.class, () -> standardLoader.load("a=\""));
        assertThrows(ParsingException.class, () -> standardLoader.load("a=("));
        assertThrows(ParsingException.class, () -> standardLoader.load("a=["));
        assertThrows(ParsingException.class, () -> standardLoader.load("a=aaa"));
        assertThrows(ParsingException.class, () -> standardLoader.load("a=1..1"));
        assertThrows(ParsingException.class, () -> standardLoader.load("a=--1.1"));
        assertThrows(ParsingException.class, () -> standardLoader.load("a=[1,1/1]"));
        assertThrows(ParsingException.class, () -> standardLoader.load("a=[1..1]"));
        assertThrows(ParsingException.class, () -> standardLoader.load("a=[--1.1]"));
        assertThrows(ParsingException.class, () -> standardLoader.load("a=[]"));
        assertThrows(ParsingException.class, () -> standardLoader.load("a=[/]"));
    }

    @Test
    void correctVariablesPass() {
        final Map<String, Variable<?>>[] state = new Map[]{new TreeMap<>()};//cause lambda
        assertDoesNotThrow(() -> state[0] = standardLoader.load("a=1;b=-1;c=1.1;d=-1.1;e=\"\";f=\"\"\";g=(1);h=(1,-1,1.1,-1.1);i=[1];j=[1/-1/1.1/-1.1];k=[1,-1/1.1,-1.1];l=[1,-1,1.1,-1.1]"));
        assertEquals(state[0].size(), 12);
        assertEquals(state[0].get("a").getValue(), 1d);
        assertEquals(state[0].get("b").getValue(), -1d);
        assertEquals(state[0].get("c").getValue(), 1.1d);
        assertEquals(state[0].get("d").getValue(), -1.1d);
        assertEquals(state[0].get("e").getValue(), (""));
        assertEquals(state[0].get("f").getValue(), ("\""));
        assertEquals(state[0].get("g").getValue(), ("(1)"));
        assertEquals(state[0].get("h").getValue(), ("(1,-1,1.1,-1.1)"));
        assertEquals(state[0].get("i"), new MatrixVariable(new double[][]{{1}}));
        assertEquals(state[0].get("j"), new MatrixVariable(new double[][]{{1}, {-1}, {1.1}, {-1.1}}));
        assertEquals(state[0].get("k"), new MatrixVariable(new double[][]{{1, -1}, {1.1, -1.1}}));
        assertEquals(state[0].get("l"), new MatrixVariable(new double[][]{{1, -1, 1.1, -1.1}}));
    }

    /* farewell constants, I won't miss you
        @Test
        void replaceConstants() {
            final State[] state = new State[1];
            assertDoesNotThrow(() -> state[0] = standardLoader.load("", "(-1)+(-1.1)+1.1+-1+-1.1+--1+---1+--1.1+---1.1"));
            assertEquals(state[0].expressions.size() - state[0].getComputationOrder().size(), 4);
            assertEquals(state[0].expressions.get("01d0").getVariable().getValue(), 1d);
            assertEquals(state[0].expressions.get("0m1d0").getVariable().getValue(), -1d);
            assertEquals(state[0].expressions.get("01d1").getVariable().getValue(), 1.1d);
            assertEquals(state[0].expressions.get("0m1d1").getVariable().getValue(), -1.1d);
            assertDoesNotThrow(() -> state[0] = standardLoader.load("", "-+--+---+-1.1"));
            assertEquals(state[0].expressions.size() - state[0].getComputationOrder().size(), 1);
            assertEquals(state[0].expressions.get("0m1d1").getVariable().getValue(), -1.1d);
        }
    */
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
