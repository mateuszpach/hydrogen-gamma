package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.tilefactories.MatrixAndTextTileFactory;
import hydrogengamma.model.modules.utils.LinearAlgebra;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;


public class LUDecomposerTest {

    TilesContainer container =  Mockito.mock(TilesContainer.class);
    MatrixAndTextTileFactory factory = Mockito.mock(MatrixAndTextTileFactory.class);
    Tile lTile = Mockito.mock(Tile.class);
    Tile uTile = Mockito.mock(Tile.class);
    Tile luNotFoundTile = Mockito.mock(Tile.class);

    @Test
    void throwingMatrixNotSquare() {
        MatrixVariable a = new MatrixVariable(new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}});
        MatrixVariable b = new MatrixVariable(new double[][]{{1.0, 1.0}});
        MatrixVariable c = new MatrixVariable(new double[][]{{1.0}, {1.0}});

        LUDecomposer lu = new LUDecomposer(factory);

        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> lu.execute(container, a));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> lu.execute(container, b));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> lu.execute(container, c));
    }

    // TODO test L and U stored in kafelki. Commented out are old tests from static LinearAlgebra module.

    @Test
    void decompositionLUHardMatrices() {
        MatrixVariable a = new MatrixVariable(new double[][]{{0.0, 1.0}, {1.0, 2.0}});
        MatrixVariable b = new MatrixVariable(new double[][]{{Math.PI, Math.PI, Math.PI}, {Math.PI, Math.PI, 2.234}, {0.5 * Math.PI, 1.75, 3.356e7}});
        MatrixVariable c = new MatrixVariable(new double[][]{{0.0, 0.0}, {Double.MIN_VALUE, Double.MIN_VALUE}});

        LUDecomposer lu = new LUDecomposer(factory);

        lu.execute(container, a);
        lu.execute(container, b);
        lu.execute(container, c);

        Mockito.verify(factory, Mockito.times(3))
                .getTextTile(new TextVariable("LU decomposition could not be found"), "LU not found");
        Mockito.verifyNoMoreInteractions(factory);
    }

    @Test
    void properDecompositionsLU() {
        MatrixVariable a = new MatrixVariable(new double[][]{{1.0, 1.0, 1.0}, {4.0, 3.0, -1.0}, {3.0, 5.0, 3.0}});
        MatrixVariable b = new MatrixVariable(new double[][]{{1.0, 0.0, 0.0, 0.0}, {0.0, 1.0, 0.0, 0.0}, {0.0, 0.0, 1.0, 0.0}, {0.0, 0.0, 0.0, 1.0}});
        MatrixVariable c = new MatrixVariable(new double[][]{{2.0, 4.0, 3.0, 5.0}, {-4.0, -7.0, -5.0, -8.0}, {6.0, 8.0, 2.0, 9.0}, {4.0, 9.0, -2.0, 14.0}});
        MatrixVariable expectedL1 = new MatrixVariable(new double[][]{{1.0, 0.0, 0.0}, {4.0, 1.0, 0.0}, {3.0, -2.0, 1.0}});
        MatrixVariable expectedU1 = new MatrixVariable(new double[][]{{1.0, 1.0, 1.0}, {0.0, -1.0, -5.0}, {0.0, 0.0, -10.0}});
        MatrixVariable expectedL2 = new MatrixVariable(b.getValue());
        MatrixVariable expectedU2 = new MatrixVariable(b.getValue());
        MatrixVariable expectedL3 = new MatrixVariable(new double[][]{{1.0, 0.0, 0.0, 0.0}, {-2.0, 1.0, 0.0, 0.0}, {3.0, -4.0, 1.0, 0.0}, {2.0, 1.0, 3.0, 1.0}});
        MatrixVariable expectedU3 = new MatrixVariable(new double[][]{{2.0, 4.0, 3.0, 5.0}, {0.0, 1.0, 1.0, 2.0}, {0.0, 0.0, -3.0, 2.0}, {0.0, 0.0, 0.0, -4.0}});

        LUDecomposer lu = new LUDecomposer(factory);

        lu.execute(container, a);
        lu.execute(container, b);
        lu.execute(container, c);

        InOrder factOrd = Mockito.inOrder(factory);
        factOrd.verify(factory).getMatrixTile(expectedL1, "L from LU decomposition of");
        factOrd.verify(factory).getMatrixTile(expectedU1, "U from LU decomposition of");
        factOrd.verify(factory).getMatrixTile(expectedL2, "L from LU decomposition of");
        factOrd.verify(factory).getMatrixTile(expectedU2, "U from LU decomposition of");
        factOrd.verify(factory).getMatrixTile(expectedL3, "L from LU decomposition of");
        factOrd.verify(factory).getMatrixTile(expectedU3, "U from LU decomposition of");
        factOrd.verifyNoMoreInteractions();
    }

    @Test
    void containerCommunicationLUFound() {
        MatrixVariable a = new MatrixVariable(new double[][]{{1.0, 1.0, 1.0}, {4.0, 3.0, -1.0}, {3.0, 5.0, 3.0}});
        MatrixVariable expectedL1 = new MatrixVariable(new double[][]{{1.0, 0.0, 0.0}, {4.0, 1.0, 0.0}, {3.0, -2.0, 1.0}});
        MatrixVariable expectedU1 = new MatrixVariable(new double[][]{{1.0, 1.0, 1.0}, {0.0, -1.0, -5.0}, {0.0, 0.0, -10.0}});
        LUDecomposer lu = new LUDecomposer(factory);

        Mockito.when(factory.getMatrixTile(expectedL1, "L from LU decomposition of")).then((x) -> lTile);
        Mockito.when(factory.getMatrixTile(expectedU1, "U from LU decomposition of")).then((x) -> uTile);

        lu.execute(container, a);

        InOrder contOrd = Mockito.inOrder(container);
        contOrd.verify(container).addTile(lTile);
        contOrd.verify(container).addTile(uTile);
        contOrd.verifyNoMoreInteractions();
    }

    @Test
    void containerCommunicationLUNotFound() {
        MatrixVariable a = new MatrixVariable(new double[][]{{0.0, 1.0}, {1.0, 2.0}});
        LUDecomposer lu = new LUDecomposer(factory);
        Mockito.when(factory.getTextTile(new TextVariable("LU decomposition could not be found"), "LU not found")).
                then((x) -> luNotFoundTile);

        lu.execute(container, a);

        Mockito.verify(container).addTile(luNotFoundTile);
        Mockito.verifyNoMoreInteractions(container);
    }

    @Test
    public void verifyTest() {
        MatrixVariable m = new MatrixVariable(new double[][]{{0, 1}, {1, 0}});
        Variable<?>[] arr1 = new Variable[]{m};
        Variable<?>[] arr2 = new Variable[]{m, m};
        Variable<?>[] arr3 = new Variable[]{new FunctionVariable("sin(x)")};

        assertTrue(new LUDecomposer(factory).verify(arr1));
        assertFalse(new LUDecomposer(factory).verify(arr2));
        assertFalse(new LUDecomposer(factory).verify(arr3));
    }
}
