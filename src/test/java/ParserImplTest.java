import model.ParserImpl;
import model.VarBox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParserImplTest {
    @Test
    void loadsModulesSuccessfully() {
        ParserImpl parser = null;
        assertDoesNotThrow(ParserImpl::new);
    }

    @Test
    void removeWhitespaceAndHashtags() {
        ParserImpl parser = new ParserImpl();
        parser.parse("#a#a#=#1#;b    b   =   1   ;\nc\nc\n=\n1\n;\rd\r\rd\r=\r1\r; e e = 1 ", "");
        assertTrue(parser.varBoxes.containsKey("aa"));
        assertTrue(parser.varBoxes.containsKey("bb"));
        assertTrue(parser.varBoxes.containsKey("cc"));
        assertTrue(parser.varBoxes.containsKey("dd"));
        assertTrue(parser.varBoxes.containsKey("ee"));
        assertEquals(parser.varBoxes.size(), 5);
    }

    @Test
    void parseVariableDefinitionStructure() {
        ParserImpl parser = new ParserImpl();
        //returns message on throw (to parser to print status for user)
        assertNotNull(parser.load("a;1", ""));
        assertNotNull(parser.load("a=\"", ""));
        assertNotNull(parser.load("a=(", ""));
        assertNotNull(parser.load("a=[", ""));
        assertNotNull(parser.load("a=aaa", ""));
        assertNotNull(parser.load("a=1..1", ""));
        assertNotNull(parser.load("a=--1.1", ""));
        assertNotNull(parser.load("a=[1,1/1]", ""));
        assertNotNull(parser.load("a=[1..1]", ""));
        assertNotNull(parser.load("a=[--1.1]", ""));
        assertNotNull(parser.load("a=[]", ""));
        assertNotNull(parser.load("a=[/]", ""));
    }

    @Test
    void correctVariablesPass() {
        ParserImpl parser = new ParserImpl();
        assertDoesNotThrow(() -> parser.parse("a=1;b=-1;c=1.1;d=-1.1;e=\"\";f=\"\"\";g=(1);h=(1,-1,1.1,-1.1);i=[1];j=[1/-1/1.1/-1.1];k=[1,-1/1.1,-1.1];l=[1,-1,1.1,-1.1]", ""));
        //extend this test
        assertEquals(parser.varBoxes.size(), 12);
    }

    @Test
    void operationsAreSimplifiedCorrectly() {//input is changed into list of model.variables in postorder with recipes
        ParserImpl parser = new ParserImpl();
        assertDoesNotThrow(() -> parser.parse("a=1", "+(+(+(a),a),a,+(a,+(a)))"));
        assertEquals(parser.varBoxes.size(), 1 + parser.futureVariables.size());
        assertEquals(parser.futureVariables.get("#4").first, "+");
        assertEquals(parser.futureVariables.get("#4").second.size(), 3);
        assertEquals(parser.futureVariables.get("#4").second.get(0), "#1");
        assertEquals(parser.futureVariables.get("#4").second.get(1), "a");
        assertEquals(parser.futureVariables.get("#4").second.get(2), "#3");
        assertEquals(parser.varBoxes.get("#4").getValue(), 5d);
        assertEquals(parser.futureVariables.get("#3").first, "+");
        assertEquals(parser.futureVariables.get("#3").second.size(), 2);
        assertEquals(parser.futureVariables.get("#3").second.get(0), "a");
        assertEquals(parser.futureVariables.get("#3").second.get(1), "#2");
        assertEquals(parser.varBoxes.get("#3").getValue(), 2d);
        assertEquals(parser.futureVariables.get("#2").first, "+");
        assertEquals(parser.futureVariables.get("#2").second.size(), 1);
        assertEquals(parser.futureVariables.get("#2").second.get(0), "a");
        assertEquals(parser.varBoxes.get("#2").getValue(), 1d);
        assertEquals(parser.futureVariables.get("#1").first, "+");
        assertEquals(parser.futureVariables.get("#1").second.size(), 2);
        assertEquals(parser.futureVariables.get("#1").second.get(0), "#0");
        assertEquals(parser.futureVariables.get("#1").second.get(1), "a");
        assertEquals(parser.varBoxes.get("#1").getValue(), 2d);
        assertEquals(parser.futureVariables.get("#0").first, "+");
        assertEquals(parser.futureVariables.get("#0").second.size(), 1);
        assertEquals(parser.futureVariables.get("#0").second.get(0), "a");
        assertEquals(parser.varBoxes.get("#0").getValue(), 1d);
        assertEquals(parser.varBoxes.get("a").getValue(), 1d);
    }

}
