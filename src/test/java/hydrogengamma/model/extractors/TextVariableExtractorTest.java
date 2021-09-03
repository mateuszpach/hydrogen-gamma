package hydrogengamma.model.extractors;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TextVariableExtractorTest {

    @Test
    public void verifyTest() {
        String e1 = "\"\"";
        String e2 = "\"siybc48eu  bi\"";
        String e3 = "\"siybc48e\"u  bi\"";
        String e4 = "\"aihf98";
        String e5 = "a09h  2\"";
        String e6 = "a09h  2";

        TextVariableExtractor t = new TextVariableExtractor();

        assertTrue(t.verify(e1));
        assertTrue(t.verify(e2));
        assertTrue(t.verify(e3));
        assertFalse(t.verify(e4));
        assertFalse(t.verify(e5));
        assertFalse(t.verify(e6));
    }

    @Test
    public void extractTest() {
        String e1 = "\"\"";
        String e2 = "\"si 123uh9  bi\"";

        TextVariableExtractor t = new TextVariableExtractor();

        assertEquals("", t.extract(e1).getValue()); // this extractor removes ""
        assertEquals("si 123uh9  bi", t.extract(e2).getValue());
    }

}
