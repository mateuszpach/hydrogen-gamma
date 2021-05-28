import static org.junit.jupiter.api.Assertions.*;

import model.modules.LinearSystemSolver;
import model.modules.TilesContainer;
import model.modules.utils.LinearAlgebra;
import model.variables.MatrixVariable;
import org.junit.jupiter.api.Test;


public class LinearSystemSolverTest {

    @Test
    void throwingMatrixNotSquare() {
        double[][] a = {{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}};
        double[][] b = {{1.0, 1.0}};
        double[][] c = {{1.0}, {1.0}};
        TilesContainer container = new TilesContainer() {
            @Override
            public void storeTile() {}
        };

        LinearSystemSolver solver = new LinearSystemSolver();

        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> solver.execute(container, new MatrixVariable(a), null));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> solver.execute(container, new MatrixVariable(b), null));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> solver.execute(container, new MatrixVariable(c), null));
    }

    @Test
    void properlySolvedLinearSystems() {
        MatrixVariable A = new MatrixVariable(new double[][] {{1.0, -1.0}, {2.0, 1.0}});
        MatrixVariable B = new MatrixVariable(new double[][] {{3.0}, {9.0}});
        MatrixVariable sol1 = new MatrixVariable(new double[][] {{4.0}, {1.0}});
        MatrixVariable C = new MatrixVariable(new double[][] {{1.0, 1.0, -1.0}, {0.0, 1.0, 3.0}, {-1.0, 0.0, -2.0}});
        MatrixVariable D = new MatrixVariable(new double[][] {{9.0}, {3.0}, {2.0}});
        MatrixVariable sol2 = new MatrixVariable(new double[][] {{2.0 / 3.0}, {7.0}, {-4.0 / 3.0}});
        TilesContainer container = new TilesContainer() {
            @Override
            public void storeTile() {}
        };

        LinearSystemSolver solver = new LinearSystemSolver();

        MatrixVariable x = solver.execute(container, C, D);
        assertEquals(sol1, solver.execute(container, A, B));
        for (int i = 0; i < 3; i++) {
            assertTrue(Math.abs(x.get(i, 0) - sol2.get(i, 0)) < 1e-10);
        }
    }
}
