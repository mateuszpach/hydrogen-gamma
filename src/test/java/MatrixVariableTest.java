import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class MatrixVariableTest {

    @Test
    void throwingIllegalArgumentWhenEmpty() {
        double[][] zeroRows = {};
        double[][] zeroCols = {{}};

        assertThrows(IllegalArgumentException.class, () -> new MatrixVariable(null));
        assertThrows(IllegalArgumentException.class, () -> new MatrixVariable(zeroRows));
        assertThrows(IllegalArgumentException.class, () -> new MatrixVariable(zeroCols));
    }

    @Test
    void throwingIllegalArgumentWhenColumnNumberMismatches() {
        double[][] badMatrix1 = {{1.0}, {1.0, 2.0}, {1.0, 2.0, 3.0}};
        double[][] badMatrix2 = {{1.0, 0.0}, {1.0, 2.0}, {1.0, 2.0}, {1.0}};
        double[][] badMatrix3 = {{1.0, 0.0}, {1.0, 2.0}, {1.0, 2.0}, {}};

        assertThrows(IllegalArgumentException.class, () -> new MatrixVariable(badMatrix1));
        assertThrows(IllegalArgumentException.class, () -> new MatrixVariable(badMatrix2));
        assertThrows(IllegalArgumentException.class, () -> new MatrixVariable(badMatrix3));
    }

    @Test
    void properSizes() {
        double[][] a = {{1.0, 2.0, 3.0}};
        double[][] b = {{1.0}, {2.0}, {3.0}};
        double[][] c = {{0.0, 0.0, 0.0, 0.0}, {1.0, 3.0, 1.0, 1.0}, {2.0, 2.0, 2.0, 2.0}};
        MatrixVariable A = new MatrixVariable(a);
        MatrixVariable B = new MatrixVariable(b);
        MatrixVariable C = new MatrixVariable(c);

        assertEquals(1, A.rowsNum());
        assertEquals(3, A.colsNum());
        assertEquals(3, B.rowsNum());
        assertEquals(1, B.colsNum());
        assertEquals(3, C.rowsNum());
        assertEquals(4, C.colsNum());
    }

    @Test
    void properValues() {
        double[][] a = {{Double.MAX_VALUE, Double.MIN_VALUE, 0.0},
                        {5.9999999999999999, -Double.MAX_VALUE, -Double.MIN_VALUE},
                        {1e38, -4e-27, Math.PI}};
        MatrixVariable A = new MatrixVariable(a);

        for (int i = 0; i < A.rowsNum(); i++) {
            for (int j = 0; j < A.colsNum(); j++) {
                assertEquals(a[i][j], A.get(i, j));
            }
        }
    }
}
