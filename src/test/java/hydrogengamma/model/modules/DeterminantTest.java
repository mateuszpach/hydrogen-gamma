package hydrogengamma.model.modules;

import hydrogengamma.model.modules.tilefactories.NumericTileFactory;
import hydrogengamma.model.modules.utils.ModuleException;
import hydrogengamma.model.parsers.standard.TilesContainer;
import hydrogengamma.model.parsers.standard.Variable;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.model.variables.NumericVariable;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;


public class DeterminantTest {

    TilesContainer container =  Mockito.mock(TilesContainer.class);
    NumericTileFactory factory = Mockito.mock(NumericTileFactory.class);
    Tile createdTile = Mockito.mock(Tile.class);

    @Test
    void throwingMatrixNotSquare() {
        double[][] a = {{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}};
        double[][] b = {{1.0, 1.0}};
        double[][] c = {{1.0}, {1.0}};
        Determinant det = new Determinant(factory);

        assertThrows(ModuleException.class, () -> det.execute(container, new MatrixVariable(a)));
        assertThrows(ModuleException.class, () -> det.execute(container, new MatrixVariable(b)));
        assertThrows(ModuleException.class, () -> det.execute(container, new MatrixVariable(c)));
        Mockito.verifyNoInteractions(container, factory);
    }

    @Test
    void correctResult() {
        MatrixVariable A = new MatrixVariable(new double[][]{{3.0, 4.0}, {-1.0, 30.0}});
        MatrixVariable B = new MatrixVariable(new double[][]{{1.0}});
        MatrixVariable C = new MatrixVariable(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        MatrixVariable D = new MatrixVariable(new double[][]{{1.0, 2.0}, {3.0, 3.0}});
        Determinant det = new Determinant(factory);

        NumericVariable w1 = det.execute(container, A);
        NumericVariable w2 = det.execute(container, B);
        NumericVariable w3 = det.execute(container, C);
        NumericVariable w4 = det.execute(container, D);

        assertTrue(w1.getValue() - 94.0 < 1e-10);
        assertTrue(w2.getValue() - 1.0 < 1e-10);
        assertTrue(w3.getValue() - 0.0 < 1e-10);
        assertTrue(w4.getValue() - -3.0 < 1e-10);
    }

    @Test
    void factoryCommunication() {
        MatrixVariable A = new MatrixVariable(new double[][]{{3.0, 4.0}, {-1.0, 30.0}});
        MatrixVariable B = new MatrixVariable(new double[][]{{1.0}});
        Determinant det = new Determinant(factory);

        NumericVariable w1 = det.execute(container, A);
        NumericVariable w2 = det.execute(container, B);

        InOrder factOrd = Mockito.inOrder(factory);
        factOrd.verify(factory).getNumericTile(w1, "Determinant of");
        factOrd.verify(factory).getNumericTile(w2, "Determinant of");
        Mockito.verifyNoMoreInteractions(factory);
    }

    @Test
    void containerCommunication() {
        MatrixVariable A = new MatrixVariable(new double[][]{{3.0, 4.0}, {-1.0, 30.0}});
        Determinant det = new Determinant(factory);
        Mockito.when(factory.getNumericTile(new NumericVariable(94.0), "Determinant of")).then((x) -> createdTile);

        det.execute(container, A);

        Mockito.verify(container).addTile(createdTile);
        Mockito.verifyNoMoreInteractions(container);
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
}
