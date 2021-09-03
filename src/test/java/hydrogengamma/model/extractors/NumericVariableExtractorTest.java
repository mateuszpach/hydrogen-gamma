package hydrogengamma.model.extractors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumericVariableExtractorTest {
    @Test
    public void verifyTest() {
        String e1 = "293840";
        String e2 = "90314.0193";
        String e3 = "000000000000.00";
        String e4 = "1d13";
        String e5 = "hello";
        String e6 = "09  2";

        NumericVariableExtractor t = new NumericVariableExtractor();

        assertTrue(t.verify(e1));
        assertTrue(t.verify(e2));
        assertTrue(t.verify(e3));
        assertFalse(t.verify(e4));
        assertFalse(t.verify(e5));
        assertFalse(t.verify(e6));
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
