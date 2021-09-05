package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.tilefactories.MatrixTileFactory;
import hydrogengamma.model.modules.utils.ModuleException;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;


public class LinearSystemSolverTest {

    TilesContainer container =  Mockito.mock(TilesContainer.class);
    MatrixTileFactory factory = Mockito.mock(MatrixTileFactory.class);
    Tile createdTile = Mockito.mock(Tile.class);

    @Test
    void throwingMatrixNotSquare() {
        double[][] a = {{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}};
        double[][] b = {{1.0, 1.0}};
        double[][] c = {{1.0}, {1.0}};

        LinearSystemSolver solver = new LinearSystemSolver(factory);

        assertThrows(ModuleException.class, () -> solver.execute(container, new MatrixVariable(a), null));
        assertThrows(ModuleException.class, () -> solver.execute(container, new MatrixVariable(b), null));
        assertThrows(ModuleException.class, () -> solver.execute(container, new MatrixVariable(c), null));
        Mockito.verifyNoInteractions(factory, container);
    }

    @Test
    void properlySolvedLinearSystems() {
        MatrixVariable A = new MatrixVariable(new double[][] {{1.0, -1.0}, {2.0, 1.0}});
        MatrixVariable B = new MatrixVariable(new double[][] {{3.0}, {9.0}});
        MatrixVariable sol1 = new MatrixVariable(new double[][] {{4.0}, {1.0}});
        MatrixVariable C = new MatrixVariable(new double[][] {{1.0, 1.0, -1.0}, {0.0, 1.0, 3.0}, {-1.0, 0.0, -2.0}});
        MatrixVariable D = new MatrixVariable(new double[][] {{9.0}, {3.0}, {2.0}});
        MatrixVariable sol2 = new MatrixVariable(new double[][] {{2.0 / 3.0}, {7.0}, {-4.0 / 3.0}});
        LinearSystemSolver solver = new LinearSystemSolver(factory);

        MatrixVariable w1 = solver.execute(container, A, B);
        MatrixVariable w2 = solver.execute(container, C, D);

        assertEquals(sol1, w1);
        for (int i = 0; i < 3; i++) {
            assertTrue(Math.abs(w2.get(i, 0) - sol2.get(i, 0)) < 1e-10);
        }
    }

    @Test
    void factoryCommunication() {
        MatrixVariable A = new MatrixVariable(new double[][] {{1.0, -1.0}, {2.0, 1.0}});
        MatrixVariable B = new MatrixVariable(new double[][] {{3.0}, {9.0}});
        MatrixVariable sol1 = new MatrixVariable(new double[][] {{4.0}, {1.0}});
        LinearSystemSolver lin = new LinearSystemSolver(factory);

        MatrixVariable w1 = lin.execute(container, A, B);

        Mockito.verify(factory).getMatrixTile(w1, "AX=B solution where A, B are");
        Mockito.verifyNoMoreInteractions(factory);
    }

    @Test
    void containerCommunication() {
        MatrixVariable A = new MatrixVariable(new double[][] {{1.0, -1.0}, {2.0, 1.0}});
        MatrixVariable B = new MatrixVariable(new double[][] {{3.0}, {9.0}});
        MatrixVariable sol1 = new MatrixVariable(new double[][] {{4.0}, {1.0}});
        LinearSystemSolver lin = new LinearSystemSolver(factory);
        Mockito.when(factory.getMatrixTile(sol1, "AX=B solution where A, B are")).then((x) -> createdTile);

        lin.execute(container, A, B);

        Mockito.verify(container).addTile(createdTile);
        Mockito.verifyNoMoreInteractions(container);
    }


    @Test
    public void verifyTest() {
        MatrixVariable m = new MatrixVariable(new double[][]{{0, 1}, {1, 0}});
        Variable<?>[] arr1 = new Variable[]{m};
        Variable<?>[] arr2 = new Variable[]{m, m};
        Variable<?>[] arr3 = new Variable[]{new FunctionVariable("sin(x)")};

        assertFalse(new LinearSystemSolver(factory).verify(arr1));
        assertTrue(new LinearSystemSolver(factory).verify(arr2));
        assertFalse(new LinearSystemSolver(factory).verify(arr3));
    }
}
