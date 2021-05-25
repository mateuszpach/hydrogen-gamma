import model.Parser;
import model.VarBox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
    @Test
    void removeWhitespaceAndHashtags() {
        Parser parser = new Parser();
        parser.load("#a#a#:#1#;b    b   :   1   ;\nc\nc\n:\n1\n;\rd\r\rd\r:\r1\r; e e : 1 ", "");
        assertTrue(parser.variables.containsKey("aa"));
        assertTrue(parser.variables.containsKey("bb"));
        assertTrue(parser.variables.containsKey("cc"));
        assertTrue(parser.variables.containsKey("dd"));
        assertTrue(parser.variables.containsKey("ee"));
        assertEquals(parser.variables.size(), 5);
    }

    @Test
    void parseVariableDefinitionStructure() {
        Parser parser = new Parser();
        assertThrows(IllegalArgumentException.class, () -> parser.load("a;1", ""));
        assertThrows(IllegalArgumentException.class, () -> parser.load("a:\"", ""));
        assertThrows(IllegalArgumentException.class, () -> parser.load("a:(", ""));
        assertThrows(IllegalArgumentException.class, () -> parser.load("a:[", ""));
        assertThrows(IllegalArgumentException.class, () -> parser.load("a:aaa", ""));
        assertThrows(IllegalArgumentException.class, () -> parser.load("a:1..1", ""));
        assertThrows(IllegalArgumentException.class, () -> parser.load("a:--1.1", ""));
        assertThrows(IllegalArgumentException.class, () -> parser.load("a:(1..1)", ""));
        assertThrows(IllegalArgumentException.class, () -> parser.load("a:(--1.1)", ""));
        assertThrows(IllegalArgumentException.class, () -> parser.load("a:()", ""));
        assertThrows(IllegalArgumentException.class, () -> parser.load("a:[1,1/1]", ""));
        assertThrows(IllegalArgumentException.class, () -> parser.load("a:[1..1]", ""));
        assertThrows(IllegalArgumentException.class, () -> parser.load("a:[--1.1]", ""));
        assertThrows(IllegalArgumentException.class, () -> parser.load("a:[]", ""));
        assertThrows(IllegalArgumentException.class, () -> parser.load("a:[/]", ""));
    }

    @Test
    void correctVariablesPassWithCorrectType() {
        Parser parser = new Parser();
        assertDoesNotThrow(() -> parser.load("a:1;b:-1;c:1.1;d:-1.1;e:\"\";f:\"\"\";g:(1);h:(1,-1,1.1,-1.1);i:[1];j:[1/-1/1.1/-1.1];k:[1,-1/1.1,-1.1];l:[1,-1,1.1,-1.1]", ""));

        assertEquals(parser.variables.get("a").getType(), VarBox.VarType.NUMBER);
        assertEquals(parser.variables.get("b").getType(), VarBox.VarType.NUMBER);
        assertEquals(parser.variables.get("c").getType(), VarBox.VarType.NUMBER);
        assertEquals(parser.variables.get("d").getType(), VarBox.VarType.NUMBER);
        //assertEquals(parser.variables.get("e").getType(), VarBox.VarType.TEXT);
        //assertEquals(parser.variables.get("f").getType(), VarBox.VarType.TEXT);
        assertEquals(parser.variables.get("g").getType(), VarBox.VarType.FUNCTION);
        assertEquals(parser.variables.get("h").getType(), VarBox.VarType.FUNCTION);
        assertEquals(parser.variables.get("i").getType(), VarBox.VarType.MATRIX);
        assertEquals(parser.variables.get("j").getType(), VarBox.VarType.MATRIX);
        assertEquals(parser.variables.get("k").getType(), VarBox.VarType.MATRIX);
        assertEquals(parser.variables.get("l").getType(), VarBox.VarType.MATRIX);
        assertEquals(parser.variables.size(), 10);
    }

    @Test
    void operationsAreSimplifiedCorrectly() {//input is changed into list of model.variables in postorder with recipes
        Parser parser = new Parser();
        assertDoesNotThrow(() -> parser.load("a:1", "+(+(+(a),a),a,+(a,+(a)))"));
        assertEquals(parser.variables.get("a").getType(), VarBox.VarType.NUMBER);
        assertEquals(parser.variables.size(), 1);
        assertEquals(parser.futureVariables.get("#4").first, "+");
        assertEquals(parser.futureVariables.get("#4").second.size(), 3);
        assertEquals(parser.futureVariables.get("#4").second.get(0), "#1");
        assertEquals(parser.futureVariables.get("#4").second.get(1), "a");
        assertEquals(parser.futureVariables.get("#4").second.get(2), "#3");
        assertEquals(parser.futureVariables.get("#3").first, "+");
        assertEquals(parser.futureVariables.get("#3").second.size(), 2);
        assertEquals(parser.futureVariables.get("#3").second.get(0), "a");
        assertEquals(parser.futureVariables.get("#3").second.get(1), "#2");
        assertEquals(parser.futureVariables.get("#2").first, "+");
        assertEquals(parser.futureVariables.get("#2").second.size(), 1);
        assertEquals(parser.futureVariables.get("#2").second.get(0), "a");
        assertEquals(parser.futureVariables.get("#1").first, "+");
        assertEquals(parser.futureVariables.get("#1").second.size(), 2);
        assertEquals(parser.futureVariables.get("#1").second.get(0), "#0");
        assertEquals(parser.futureVariables.get("#1").second.get(1), "a");
        assertEquals(parser.futureVariables.get("#0").first, "+");
        assertEquals(parser.futureVariables.get("#0").second.size(), 1);
        assertEquals(parser.futureVariables.get("#0").second.get(0), "a");
        //parser.load("a:1","+(a,"); something like this is parsed to #0=+(a)
        //at this moment parser can guess if someone forgot finishing ))) or put excessive ,
        //is it a feature or a bug?
    }

}
