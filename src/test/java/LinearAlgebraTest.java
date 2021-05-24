import model.modules.LinearAlgebra;
import org.junit.jupiter.api.Test;
import utils.Pair;
import model.variables.MatrixVariable;

import static org.junit.jupiter.api.Assertions.*;

public class LinearAlgebraTest {

    @Test
    void throwingMatrixNotSquare() {
        double[][] a = {{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}};
        double[][] b = {{1.0, 1.0}};
        double[][] c = {{1.0}, {1.0}};

        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> LinearAlgebra.decompositionLU(new MatrixVariable(a)));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> LinearAlgebra.decompositionLU(new MatrixVariable(b)));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> LinearAlgebra.decompositionLU(new MatrixVariable(c)));

        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> LinearAlgebra.determinant(new MatrixVariable(a)));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> LinearAlgebra.determinant(new MatrixVariable(b)));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> LinearAlgebra.determinant(new MatrixVariable(c)));

        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> LinearAlgebra.solveLinearSystem(new MatrixVariable(a), null));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> LinearAlgebra.solveLinearSystem(new MatrixVariable(b), null));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> LinearAlgebra.solveLinearSystem(new MatrixVariable(c), null));
    }

    @Test
    void decompositionLUHardMatrices() {
        double[][] a = {{0.0, 1.0}, {1.0, 2.0}};
        double[][] b = {{Math.PI, Math.PI, Math.PI}, {Math.PI, Math.PI, 2.234}, {0.5 * Math.PI, 1.75, 3.356e7}};
        double[][] c = {{0.0, 0.0}, {Double.MIN_VALUE, Double.MIN_VALUE}};

        assertDoesNotThrow(() -> LinearAlgebra.decompositionLU(new MatrixVariable(a)));
        assertDoesNotThrow(() -> LinearAlgebra.decompositionLU(new MatrixVariable(b)));
        assertDoesNotThrow(() -> LinearAlgebra.decompositionLU(new MatrixVariable(c)));
    }

    @Test
    void properDecompositionsLU() {
        double[][] a = {{1.0, 1.0, 1.0}, {4.0, 3.0, -1.0}, {3.0, 5.0, 3.0}};
        double[][] b = {{1.0, 0.0, 0.0, 0.0}, {0.0, 1.0, 0.0, 0.0}, {0.0, 0.0, 1.0, 0.0}, {0.0, 0.0, 0.0, 1.0}};
        double[][] c = {{2.0, 4.0, 3.0, 5.0}, {-4.0, -7.0, -5.0, -8.0}, {6.0, 8.0, 2.0, 9.0}, {4.0, 9.0, -2.0, 14.0}};

        MatrixVariable expectedL1 = new MatrixVariable(new double[][]{{1.0, 0.0, 0.0}, {4.0, 1.0, 0.0}, {3.0, -2.0, 1.0}});
        MatrixVariable expectedU1 = new MatrixVariable(new double[][]{{1.0, 1.0, 1.0}, {0.0, -1.0, -5.0}, {0.0, 0.0, -10.0}});
        MatrixVariable expectedL2 = new MatrixVariable(b);
        MatrixVariable expectedU2 = new MatrixVariable(b.clone());
        MatrixVariable expectedL3 = new MatrixVariable(new double[][]{{1.0, 0.0, 0.0, 0.0}, {-2.0, 1.0, 0.0, 0.0}, {3.0, -4.0, 1.0, 0.0}, {2.0, 1.0, 3.0, 1.0}});
        MatrixVariable expectedU3 = new MatrixVariable(new double[][]{{2.0, 4.0, 3.0, 5.0}, {0.0, 1.0, 1.0, 2.0}, {0.0, 0.0, -3.0, 2.0}, {0.0, 0.0, 0.0, -4.0}});

        Pair<MatrixVariable, MatrixVariable> lu1 = LinearAlgebra.decompositionLU(new MatrixVariable(a));
        Pair<MatrixVariable, MatrixVariable> lu2 = LinearAlgebra.decompositionLU(new MatrixVariable(b));
        Pair<MatrixVariable, MatrixVariable> lu3 = LinearAlgebra.decompositionLU(new MatrixVariable(c));

        assertEquals(expectedL1, lu1.first);
        assertEquals(expectedU1, lu1.second);
        assertEquals(expectedL2, lu2.first);
        assertEquals(expectedU2, lu2.second);
        assertEquals(expectedL3, lu3.first);
        assertEquals(expectedU3, lu3.second);
    }

    @Test
    void properDeterminant() {
        MatrixVariable A = new MatrixVariable(new double[][] {{3.0, 4.0}, {-1.0, 30.0}});
        MatrixVariable B = new MatrixVariable(new double[][] {{1.0}});
        MatrixVariable C = new MatrixVariable(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});

        assertTrue(Math.abs(LinearAlgebra.determinant(A) - 94.0) < 1e-10);
        assertTrue(Math.abs(LinearAlgebra.determinant(B) - 1.0) < 1e-10);
        assertTrue(Math.abs(LinearAlgebra.determinant(C) - 0.0) < 1e-10);
    }

    @Test
    void properlySolvedLinearSystems() {
        MatrixVariable A = new MatrixVariable(new double[][] {{1.0, -1.0}, {2.0, 1.0}});
        MatrixVariable B = new MatrixVariable(new double[][] {{3.0}, {9.0}});
        MatrixVariable sol1 = new MatrixVariable(new double[][] {{4.0}, {1.0}});
        MatrixVariable C = new MatrixVariable(new double[][] {{1.0, 1.0, -1.0}, {0.0, 1.0, 3.0}, {-1.0, 0.0, -2.0}});
        MatrixVariable D = new MatrixVariable(new double[][] {{9.0}, {3.0}, {2.0}});
        MatrixVariable sol2 = new MatrixVariable(new double[][] {{2.0 / 3.0}, {7.0}, {-4.0 / 3.0}});
        MatrixVariable x = LinearAlgebra.solveLinearSystem(C, D);

        assertEquals(sol1, LinearAlgebra.solveLinearSystem(A, B));
        for (int i = 0; i < 3; i++) {
            assertTrue(Math.abs(x.get(i, 0) - sol2.get(i, 0)) < 1e-10);
        }
    }
}
