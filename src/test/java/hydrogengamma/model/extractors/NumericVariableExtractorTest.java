package hydrogengamma.model.extractors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NumericVariableExtractorTest {
    @Test
    public void doesNotModifyArguments() {
        final String e1 = "2 9 3 8 4 0";
        final String e2 = "2 9 3 8 4 0";
        FunctionVariableExtractor t = new FunctionVariableExtractor();
        assertDoesNotThrow(() -> t.verify(e1));
        assertEquals(e2, e1);
        assertDoesNotThrow(() -> t.extract(e1));
        assertEquals(e2, e1);
    }

    @Test
    public void verifyTest() {
        String e1 = "293840";
        String e2 = "90314.0193";
        String e3 = "000000000000.00";
        String e4 = "-293840";
        String e5 = "1d13";
        String e6 = "hello";
        String e7 = "09  2";

        NumericVariableExtractor t = new NumericVariableExtractor();

        assertTrue(t.verify(e1));
        assertTrue(t.verify(e2));
        assertTrue(t.verify(e3));
        assertTrue(t.verify(e4));
        assertFalse(t.verify(e5));
        assertFalse(t.verify(e6));
        assertFalse(t.verify(e7));
    }

    @Test
    public void extractTest() {
        String e1 = "293840";
        String e2 = "90314.0193";
        String e3 = "000000000000.00";

        NumericVariableExtractor t = new NumericVariableExtractor();

        assertEquals(293840, t.extract(e1).getValue()); // this extractor removes ""
        assertEquals(90314.0193, t.extract(e2).getValue());
        assertEquals(0, t.extract(e3).getValue());
    }
}
