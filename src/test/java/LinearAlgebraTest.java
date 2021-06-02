import model.modules.utils.LinearAlgebra;
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

        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> LinearAlgebra.decompositionLUPivoted(new MatrixVariable(a)));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> LinearAlgebra.decompositionLUPivoted(new MatrixVariable(b)));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> LinearAlgebra.decompositionLUPivoted(new MatrixVariable(c)));
    }

    @Test
    void decompositionLUPivotedTest() {
        MatrixVariable a = new MatrixVariable(new double[][]{{0.0, 1.0}, {1.0, 1.0}});

        Pair<Pair<MatrixVariable, MatrixVariable>, Integer[]> lu = LinearAlgebra.decompositionLUPivoted(a);

        MatrixVariable expL = new MatrixVariable(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        MatrixVariable expU = new MatrixVariable(new double[][]{{1.0, 1.0}, {0.0, 1.0}});

        assertEquals(expL, lu.first.first);
        assertEquals(expU, lu.first.second);
        assertEquals(1, lu.second[0]);
        assertEquals(0, lu.second[1]);
    }

    @Test
    void performGaussElimStepTest() {
        double[][] L = new double[][]{{0.0, 0.0}, {0.0, 0.0}};
        double[][] U = new double[][]{{2.0, -1.0}, {1.0, -3.0}};
        MatrixVariable expectL = new MatrixVariable(new double[][]{{1.0, 0.0}, {0.5, 0.0}});
        MatrixVariable expectU = new MatrixVariable(new double[][]{{2.0, -1.0}, {0.0, -2.5}});

        LinearAlgebra.performGaussElimStep(L, U, 0);

        assertEquals(expectL, new MatrixVariable(L));
        assertEquals(expectU, new MatrixVariable(U));
    }

    @Test
    void prepareLUTest() {
        MatrixVariable a = new MatrixVariable(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        MatrixVariable zeros = new MatrixVariable(new double[][]{{0.0, 0.0}, {0.0, 0.0}});

        Pair<MatrixVariable, MatrixVariable> prep = LinearAlgebra.prepareLU(a);

        assertEquals(zeros, prep.first);
        assertEquals(a, prep.second);
    }

    @Test
    void solveSystemUpperTest() {
        double[][] a = {{2.0, 5.0}, {0.0, 8.0}};
        double[][] b = {{-2.0}, {3.0}};
        Integer[] perm = new Integer[]{1, 0};

        MatrixVariable sol = LinearAlgebra.solveSystemUpper(new MatrixVariable(a), new MatrixVariable(b), perm);

        assertEquals(new MatrixVariable(new double[][]{{2.125}, {-0.25}}), sol);
    }

    @Test
    void solveSystemLowerTest() {
        double[][] a = {{2.0, 0.0}, {-3.0, 4.0}};
        double[][] b = {{1.0}, {-1.0}};
        Integer[] perm = new Integer[]{0, 1};

        MatrixVariable sol = LinearAlgebra.solveSystemLower(new MatrixVariable(a), new MatrixVariable(b), perm);

        assertEquals(new MatrixVariable(new double[][]{{0.5}, {0.125}}), sol);
    }

    @Test
    void identityTest() {
        Integer[] id1 = LinearAlgebra.identityPermutation(1);
        Integer[] id2 = LinearAlgebra.identityPermutation(5);

        assertEquals(0, id1[0]);
        for (int i = 0; i < 5; i++) {
            assertEquals(i, id2[i]);
        }
    }
}
