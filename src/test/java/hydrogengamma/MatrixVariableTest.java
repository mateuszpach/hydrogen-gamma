package hydrogengamma;

import hydrogengamma.model.variables.MatrixVariable;
import org.junit.jupiter.api.Test;

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

    @Test
    void sameMatricesEqual() {
        MatrixVariable A = new MatrixVariable(new double[][] {{1.0, -2.0}, {1e100, -3e100}});
        MatrixVariable B = new MatrixVariable(new double[][] {{1.0, -2.0}, {1e100, -3e100}});

        assertEquals(A, B);
        assertEquals(B, A);
    }

    @Test
    void differentMatricesNotEqual() {
        MatrixVariable A = new MatrixVariable(new double[][]{{1e-20, -2.0}, {1e100, -3e100}});
        MatrixVariable B = new MatrixVariable(new double[][]{{-1e-20, -2.0}, {1e100, -3e100}});

        assertNotEquals(A, B);
        assertNotEquals(B, A);
        assertNotEquals(new MatrixVariable(new double[][]{{1.0, 1.0}}), new MatrixVariable(new double[][]{{1.0}}));
        assertNotEquals(new MatrixVariable(new double[][]{{1.0, 1.0}}), new MatrixVariable(new double[][]{{1.0, 1.0}, {1.0, 1.0}}));
    }

    @Test
    void actsAsFinal() {// other types are ok since String is final by definition and double is passed byb value
        double[][] inside = new double[][]{{5d}};
        MatrixVariable a = new MatrixVariable(inside);
        inside[0][0] = -5d;
        assertNotEquals(inside, a.getValue());
    }
}
