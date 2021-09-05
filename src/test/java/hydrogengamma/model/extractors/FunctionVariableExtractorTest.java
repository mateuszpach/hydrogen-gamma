package hydrogengamma.model.extractors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FunctionVariableExtractorTest {
    @Test
    public void doesNotModifyArguments() {
        final String e1 = "(    )";
        final String e2 = "(    )";
        FunctionVariableExtractor t = new FunctionVariableExtractor();

        assertDoesNotThrow(() -> t.verify(e1));
        assertEquals(e2, e1);
        assertDoesNotThrow(() -> t.extract(e1));
        assertEquals(e2, e1);
    }

    @Test
    public void verifyGood() {
        String e1 = "()";
        String e2 = "(ubv9ben4 das)"; // pointless formulas are acceptable. It's the Module who throws.
        String e3 = "(siybc48e)u ((i)";

        FunctionVariableExtractor t = new FunctionVariableExtractor();

        assertTrue(t.verify(e1));
        assertTrue(t.verify(e2));
        assertTrue(t.verify(e3));
    }

    @Test
    public void verifyBad() {
        // Missing parentheses
        String e1 = "(aihf98";
        String e2 = "a09h  2)";
        String e3 = "a09h  2";

        FunctionVariableExtractor t = new FunctionVariableExtractor();

        assertFalse(t.verify(e1));
        assertFalse(t.verify(e2));
        assertFalse(t.verify(e3));
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
