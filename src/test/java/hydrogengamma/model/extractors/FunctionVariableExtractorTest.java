package hydrogengamma.model.extractors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctionVariableExtractorTest {
    @Test
    public void verifyTest() {
        String e1 = "()";
        String e2 = "(ubv9ben4 das)"; // pointless formulas are acceptable. It's the Module who throws.
        String e3 = "(siybc48e)u ((i)";
        String e4 = "(aihf98";
        String e5 = "a09h  2)";
        String e6 = "a09h  2";

        FunctionVariableExtractor t = new FunctionVariableExtractor();

        assertTrue(t.verify(e1));
        assertTrue(t.verify(e2));
        assertTrue(t.verify(e3));
        assertFalse(t.verify(e4));
        assertFalse(t.verify(e5));
        assertFalse(t.verify(e6));
    }

    @Test
    public void extractTest() {
        String e1 = "()";
        String e2 = "((si 1209h 3n))";

        FunctionVariableExtractor t = new FunctionVariableExtractor();

        assertEquals("()", t.extract(e1).getValue());
        assertEquals("((si 1209h 3n))", t.extract(e2).getValue());
    }
}
