package hydrogengamma.model.extractors;

import hydrogengamma.model.variables.MatrixVariable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixVariableExtractorTest {
    @Test
    public void doesNotModifyArguments() {
        final String e1 = "[1 /2 /3 ]";
        final String e2 = "[1 /2 /3 ]";
        FunctionVariableExtractor t = new FunctionVariableExtractor();
        assertDoesNotThrow(() -> t.verify(e1));
        assertEquals(e2, e1);
        assertDoesNotThrow(() -> t.extract(e1));
        assertEquals(e2, e1);
    }

    @Test
    public void verifyTest() {
        String e1 = "[1/2/3]";
        String e2 = "[0,0/0,,,,0/2,3,,]"; // empty fields ought to be omited
        String e3 = "[1.0,   2.0, ]";
        String e4 = "[]"; // empty not accepted
        String e5 = "hello";
        String e6 = "[2,3,4/2,3]"; // unequal row lengths
        String e7 = "[2,3,a/2,3,2]"; // a != number

        MatrixVariableExtractor t = new MatrixVariableExtractor();

        assertTrue(t.verify(e1));
        assertTrue(t.verify(e2));
        assertTrue(t.verify(e3));
        assertFalse(t.verify(e4));
        assertFalse(t.verify(e5));
        assertFalse(t.verify(e6));
        assertFalse(t.verify(e7));
    }

    @Test
    public void extractTest() {
        String e1 = "[1/2/3]";
        String e2 = "[0,0/0,,,,0/2,3,,]";
        String e3 = "[1.0,   2.0, ]";

        MatrixVariableExtractor t = new MatrixVariableExtractor();

        assertEquals(new MatrixVariable(new double[][]{{1},{2},{3}}), t.extract(e1)); // this extractor removes ""
        assertEquals(new MatrixVariable(new double[][]{{0,0},{0,0},{2,3}}), t.extract(e2));
        assertEquals(new MatrixVariable(new double[][]{{1,2}}), t.extract(e3));
    }
}
