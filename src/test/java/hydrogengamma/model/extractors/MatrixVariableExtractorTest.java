package hydrogengamma.model.extractors;

import hydrogengamma.model.variables.MatrixVariable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixVariableExtractorTest {

    MatrixVariableExtractor t = new MatrixVariableExtractor();

    @Test
    public void doesNotModifyArguments() {
        final String e1 = "[1 /2 /3 ]";
        final String e2 = "[1 /2 /3 ]";

        assertDoesNotThrow(() -> t.verify(e1));
        assertEquals(e2, e1);
        assertDoesNotThrow(() -> t.extract(e1));
        assertEquals(e2, e1);
    }

    @Test
    public void verifyNormal() {
        String e = "[1/2/3]";

        assertTrue(t.verify(e));
    }

    @Test
    public void verifyEmptySpaces() {
        String e =  "[1.0,   2.0, ]";

        assertTrue(t.verify(e));
    }

    @Test
    public void verifyEmptyFields() {
        String e = "[0,0/0,,,,0/2,3,,]"; // empty fields ought to be omitted

        assertTrue(t.verify(e));
    }

    @Test
    public void verifyEmpty() {
        String e1 = "[]";
        String e2 = "[/]";

        assertFalse(t.verify(e1));
        assertFalse(t.verify(e2));
    }

    @Test
    public void verifyBadType() {
        String e = "hello";

        assertFalse(t.verify(e));
    }

    @Test
    public void verifyUnequalRowLengths() {
        String e = "[2,3,4/2,3]";

        assertFalse(t.verify(e));
    }

    @Test
    public void verifyNaN() {
        String e = "[2,3,a/2,3,2]";

        assertFalse(t.verify(e));
    }

    @Test
    public void extractTest() {
        String e1 = "[1/2/3]";
        String e2 = "[0,0/0,,,,0/2,3,,]";
        String e3 = "[1.0,   2.0, ]";

        assertEquals(new MatrixVariable(new double[][]{{1},{2},{3}}), t.extract(e1)); // this extractor removes ""
        assertEquals(new MatrixVariable(new double[][]{{0,0},{0,0},{2,3}}), t.extract(e2));
        assertEquals(new MatrixVariable(new double[][]{{1,2}}), t.extract(e3));
    }
}
