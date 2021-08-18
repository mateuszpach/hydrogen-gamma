import model.ParserImpl;
import model.ParserImplState;
import model.VarBox;
import model.variables.FunctionVariable;
import model.variables.MatrixVariable;
import model.variables.NumericVariable;
import model.variables.TextVariable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParserImplTest {

    @Test
    void removeWhitespaceAndHashtags() {
        ParserImpl parser = new ParserImpl();
        ParserImplState state = new ParserImplState();
        parser.load("#a#a#=#1#;b    b   =   1   ;\nc\nc\n=\n1\n;\rd\r\rd\r=\r1\r; e e = 1 ", "", state);
        assertTrue(state.varBoxes.containsKey("aa"));
        assertTrue(state.varBoxes.containsKey("bb"));
        assertTrue(state.varBoxes.containsKey("cc"));
        assertTrue(state.varBoxes.containsKey("dd"));
        assertTrue(state.varBoxes.containsKey("ee"));
        assertEquals(state.varBoxes.size(), 5);
    }
    @Test
    void parseVariableDefinitionStructure() {
        ParserImpl parser = new ParserImpl();
        ParserImplState state = new ParserImplState();
        //returns message on throw (to parser to print status for user)
        assertNotNull(parser.load("a;1", "", state));
        state = new ParserImplState();
        assertNotNull(parser.load("1a;1", "", state));
        state = new ParserImplState();
        assertNotNull(parser.load("a=\"", "", state));
        state = new ParserImplState();
        assertNotNull(parser.load("a=(", "", state));
        state = new ParserImplState();
        assertNotNull(parser.load("a=[", "", state));
        state = new ParserImplState();
        assertNotNull(parser.load("a=aaa", "", state));
        state = new ParserImplState();
        assertNotNull(parser.load("a=1..1", "", state));
        state = new ParserImplState();
        assertNotNull(parser.load("a=--1.1", "", state));
        state = new ParserImplState();
        assertNotNull(parser.load("a=[1,1/1]", "", state));
        state = new ParserImplState();
        assertNotNull(parser.load("a=[1..1]", "", state));
        state = new ParserImplState();
        assertNotNull(parser.load("a=[--1.1]", "", state));
        state = new ParserImplState();
        assertNotNull(parser.load("a=[]", "", state));
        state = new ParserImplState();
        assertNotNull(parser.load("a=[/]", "", state));
    }

    @Test
    void correctVariablesPass() {
        ParserImpl parser = new ParserImpl();
        ParserImplState state = new ParserImplState();
        assertDoesNotThrow(() -> parser.load("a=1;b=-1;c=1.1;d=-1.1;e=\"\";f=\"\"\";g=(1);h=(1,-1,1.1,-1.1);i=[1];j=[1/-1/1.1/-1.1];k=[1,-1/1.1,-1.1];l=[1,-1,1.1,-1.1]", "", state));
        //TODO: extend this test
        assertEquals(state.varBoxes.size(), 12);
        assertEquals(state.varBoxes.get("a").getValue(), 1d);
        assertEquals(state.varBoxes.get("b").getValue(), -1d);
        assertEquals(state.varBoxes.get("c").getValue(), 1.1d);
        assertEquals(state.varBoxes.get("d").getValue(), -1.1d);
        assertEquals(state.varBoxes.get("e").getValue(), (""));
        assertEquals(state.varBoxes.get("f").getValue(), ("\""));
        assertEquals(state.varBoxes.get("g").getValue(), ("(1)"));
        assertEquals(state.varBoxes.get("h").getValue(), ("(1,-1,1.1,-1.1)"));
        assertEquals(state.varBoxes.get("i"), new MatrixVariable(new double[][]{{1}}));
        assertEquals(state.varBoxes.get("j"), new MatrixVariable(new double[][]{{1}, {-1}, {1.1}, {-1.1}}));
        assertEquals(state.varBoxes.get("k"), new MatrixVariable(new double[][]{{1, -1}, {1.1, -1.1}}));
        assertEquals(state.varBoxes.get("l"), new MatrixVariable(new double[][]{{1, -1, 1.1, -1.1}}));
    }

    @Test
    void replaceConstants() {
        ParserImpl parser = new ParserImpl();
        ParserImplState state = new ParserImplState();
        ParserImplState finalState = new ParserImplState();
        assertDoesNotThrow(() -> parser.load("", "1,1.1,-1,-1.1,--1,---1,--1.1,---1.1", state));
        assertEquals(state.varBoxes.size(), 4);
        assertEquals(state.varBoxes.get("##1.0").getValue(), 1d);
        assertEquals(state.varBoxes.get("##-1.0").getValue(), -1d);
        assertEquals(state.varBoxes.get("##1.1").getValue(), 1.1d);
        assertEquals(state.varBoxes.get("##-1.1").getValue(), -1.1d);
        assertDoesNotThrow(() -> parser.load("", "-,--,---,-1..1", finalState));
        assertEquals(finalState.varBoxes.size(), 2);
        assertEquals(finalState.varBoxes.get("##1.0").getValue(), 1d);
        assertEquals(finalState.varBoxes.get("##-1.0").getValue(), -1d);
    }

    @Test
    void operationsAreSimplifiedCorrectly() {//input is changed into list of model.variables in postorder with recipes
        ParserImpl parser = new ParserImpl();
        ParserImplState state = new ParserImplState();
        assertDoesNotThrow(() -> parser.load("a=1", "+(+(+(a),a),a,+(a,+(a)))", state));
        assertDoesNotThrow(() -> parser.compute(state));
        assertEquals(state.varBoxes.size(), 1 + state.futureVariables.size());
        assertEquals(state.futureVariables.get("#4").first, "+");
        assertEquals(state.futureVariables.get("#4").second.size(), 3);
        assertEquals(state.futureVariables.get("#4").second.get(0), "#1");
        assertEquals(state.futureVariables.get("#4").second.get(1), "a");
        assertEquals(state.futureVariables.get("#4").second.get(2), "#3");
        assertEquals(state.varBoxes.get("#4").getValue(), 5d);
        assertEquals(state.futureVariables.get("#3").first, "+");
        assertEquals(state.futureVariables.get("#3").second.size(), 2);
        assertEquals(state.futureVariables.get("#3").second.get(0), "a");
        assertEquals(state.futureVariables.get("#3").second.get(1), "#2");
        assertEquals(state.varBoxes.get("#3").getValue(), 2d);
        assertEquals(state.futureVariables.get("#2").first, "+");
        assertEquals(state.futureVariables.get("#2").second.size(), 1);
        assertEquals(state.futureVariables.get("#2").second.get(0), "a");
        assertEquals(state.varBoxes.get("#2").getValue(), 1d);
        assertEquals(state.futureVariables.get("#1").first, "+");
        assertEquals(state.futureVariables.get("#1").second.size(), 2);
        assertEquals(state.futureVariables.get("#1").second.get(0), "#0");
        assertEquals(state.futureVariables.get("#1").second.get(1), "a");
        assertEquals(state.varBoxes.get("#1").getValue(), 2d);
        assertEquals(state.futureVariables.get("#0").first, "+");
        assertEquals(state.futureVariables.get("#0").second.size(), 1);
        assertEquals(state.futureVariables.get("#0").second.get(0), "a");
        assertEquals(state.varBoxes.get("#0").getValue(), 1d);
        assertEquals(state.varBoxes.get("a").getValue(), 1d);
    }
}
