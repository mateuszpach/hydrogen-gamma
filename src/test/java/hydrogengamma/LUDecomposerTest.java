package hydrogengamma;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.TilesContainerImpl;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.utils.LinearAlgebra;
import hydrogengamma.model.modules.LUDecomposer;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.vartiles.MatrixTile;
import hydrogengamma.vartiles.TextTile;
import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class LUDecomposerTest {

    @Test
    void throwingMatrixNotSquare() {
        double[][] a = {{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}};
        double[][] b = {{1.0, 1.0}};
        double[][] c = {{1.0}, {1.0}};
        TilesContainer container = new TilesContainer() {
            @Override
            public void addTile(Tile tile) { }

            @Override
            public ArrayList<Tile> getTiles() { return null; }
        };

        LUDecomposer lu = new LUDecomposer();

        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> lu.execute(container, new MatrixVariable(a)));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> lu.execute(container, new MatrixVariable(b)));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> lu.execute(container, new MatrixVariable(c)));
    }

    // TODO test L and U stored in kafelki. Commented out are old tests from static LinearAlgebra module.

    @Test
    void decompositionLUHardMatrices() {
        double[][] a = {{0.0, 1.0}, {1.0, 2.0}};
        double[][] b = {{Math.PI, Math.PI, Math.PI}, {Math.PI, Math.PI, 2.234}, {0.5 * Math.PI, 1.75, 3.356e7}};
        double[][] c = {{0.0, 0.0}, {Double.MIN_VALUE, Double.MIN_VALUE}};

        LUDecomposer lu = new LUDecomposer();
        TilesContainer container = Mockito.mock(TilesContainer.class);
        ArgumentCaptor<Tile> captor = ArgumentCaptor.forClass(Tile.class);

        lu.execute(container, new MatrixVariable(a));
        lu.execute(container, new MatrixVariable(b));
        lu.execute(container, new MatrixVariable(c));

        Mockito.verify(container, Mockito.times(3)).addTile(captor.capture());
        List<Tile> tiles = captor.getAllValues();

        TextTile e = new TextTile(new TextVariable("LU decomposition could not be found"), "LU not found");
        assertEquals(e.getContent(), tiles.get(0).getContent());
        assertEquals(e.getContent(), tiles.get(1).getContent());
        assertEquals(e.getContent(), tiles.get(2).getContent());
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

        LUDecomposer lu = new LUDecomposer();
        TilesContainer container = Mockito.mock(TilesContainer.class);
        ArgumentCaptor<Tile> captor = ArgumentCaptor.forClass(Tile.class);

        lu.execute(container, new MatrixVariable(a));
        lu.execute(container, new MatrixVariable(b));
        lu.execute(container, new MatrixVariable(c));

        Mockito.verify(container, Mockito.times(6)).addTile(captor.capture());
        List<Tile> tiles = captor.getAllValues();

        assertEquals(new MatrixTile(expectedL1, "").getContent(), tiles.get(0).getContent());
        assertEquals(new MatrixTile(expectedU1, "").getContent(), tiles.get(1).getContent());
        assertEquals(new MatrixTile(expectedL2, "").getContent(), tiles.get(2).getContent());
        assertEquals(new MatrixTile(expectedU2, "").getContent(), tiles.get(3).getContent());
        assertEquals(new MatrixTile(expectedL3, "").getContent(), tiles.get(4).getContent());
        assertEquals(new MatrixTile(expectedU3, "").getContent(), tiles.get(5).getContent());
    }


    @Test
    public void verifyTest() {
        MatrixVariable m = new MatrixVariable(new double[][]{{0, 1}, {1, 0}});
        Variable<?>[] arr1 = new Variable[]{m};
        Variable<?>[] arr2 = new Variable[]{m, m};
        Variable<?>[] arr3 = new Variable[]{new FunctionVariable("sin(x)")};

        assertTrue(new LUDecomposer().verify(arr1));
        assertFalse(new LUDecomposer().verify(arr2));
        assertFalse(new LUDecomposer().verify(arr3));
    }
}
