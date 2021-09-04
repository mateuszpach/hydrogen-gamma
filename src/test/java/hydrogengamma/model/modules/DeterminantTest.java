package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.Determinant;
import hydrogengamma.model.modules.utils.LinearAlgebra;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.modules.tilefactories.NumericTileFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;


public class DeterminantTest {

    TilesContainer container =  Mockito.mock(TilesContainer.class);
    NumericTileFactory factory = Mockito.mock(NumericTileFactory.class);

    @Test
    void throwingMatrixNotSquare() {
        double[][] a = {{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}};
        double[][] b = {{1.0, 1.0}};
        double[][] c = {{1.0}, {1.0}};

        Determinant det = new Determinant(factory);

        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> det.execute(container, new MatrixVariable(a)));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> det.execute(container, new MatrixVariable(b)));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> det.execute(container, new MatrixVariable(c)));
    }

    @Test
    void properDeterminant() {
        MatrixVariable A = new MatrixVariable(new double[][]{{3.0, 4.0}, {-1.0, 30.0}});
        MatrixVariable B = new MatrixVariable(new double[][]{{1.0}});
        MatrixVariable C = new MatrixVariable(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});

        Determinant det = new Determinant(factory);

        assertTrue(Math.abs(det.execute(container, A).getValue() - 94.0) < 1e-10);
        assertTrue(Math.abs(det.execute(container, B).getValue() - 1.0) < 1e-10);
        assertTrue(Math.abs(det.execute(container, C).getValue() - 0.0) < 1e-10);
    }

    @Test
    public void verifyTest() {
        MatrixVariable m = new MatrixVariable(new double[][]{{0, 1}, {1, 0}});
        Variable<?>[] arr1 = new Variable[]{m};
        Variable<?>[] arr2 = new Variable[]{m, m};
        Variable<?>[] arr3 = new Variable[]{new FunctionVariable("sin(x)")};

        assertTrue(new Determinant(factory).verify(arr1));
        assertFalse(new Determinant(factory).verify(arr2));
        assertFalse(new Determinant(factory).verify(arr3));
    }

    @Test
    public void factoryCommunicationTest() {
        MatrixVariable A = new MatrixVariable(new double[][]{{3.0, 4.0}, {-1.0, 30.0}});
        Determinant det = new Determinant(factory);

        det.execute(container, A);

        Mockito.verify(factory).getNumericTile(new NumericVariable(94.0), "Determinant of");
    }
}
