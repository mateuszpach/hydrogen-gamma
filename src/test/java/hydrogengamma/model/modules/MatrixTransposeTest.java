package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.modules.tilefactories.MatrixAndTextTileFactory;
import hydrogengamma.model.modules.tilefactories.MatrixTileFactory;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTransposeTest {

    TilesContainer container =  Mockito.mock(TilesContainer.class);
    MatrixTileFactory factory = Mockito.mock(MatrixAndTextTileFactory.class);
    Tile createdTile = Mockito.mock(Tile.class);

    @Test
    void executeTest() {
        MatrixVariable a1 = new MatrixVariable(new double[][]{{1, 2}});
        MatrixVariable a2 = new MatrixVariable(new double[][]{{1, 2}, {1, 2}, {1, 2}});
        MatrixVariable e1 = new MatrixVariable(new double[][]{{1}, {2}});
        MatrixVariable e2 = new MatrixVariable(new double[][]{{1, 1, 1}, {2, 2, 2}});
        MatrixTranspose t = new MatrixTranspose(factory);

        MatrixVariable w1 = t.execute(container, a1);
        MatrixVariable w2 = t.execute(container, a2);

        assertEquals(e1, w1);
        assertEquals(e2, w2);
    }

    @Test
    void factoryCommunication() {
        MatrixVariable a1 = new MatrixVariable(new double[][]{{1, 2}});
        MatrixVariable e1 = new MatrixVariable(new double[][]{{1}, {2}});
        MatrixTranspose t = new MatrixTranspose(factory);

        MatrixVariable w1 = t.execute(container, a1);

        Mockito.verify(factory).getMatrixTile(e1, "Transpose of");
        Mockito.verifyNoMoreInteractions(factory);
    }

    @Test
    void containerCommunication() {
        MatrixVariable a1 = new MatrixVariable(new double[][]{{1, 2}});
        MatrixVariable e1 = new MatrixVariable(new double[][]{{1}, {2}});
        MatrixTranspose t = new MatrixTranspose(factory);
        Mockito.when(factory.getMatrixTile(e1, "Transpose of")).then((x) -> createdTile);

        t.execute(container, a1);

        Mockito.verify(container).addTile(createdTile);
        Mockito.verifyNoMoreInteractions(container);
    }

    @Test
    void verifyTest() {
        MatrixVariable a = new MatrixVariable(new double[][]{{1}});
        FunctionVariable b = new FunctionVariable("");
        MatrixTranspose t = new MatrixTranspose(factory);

        assertTrue(t.verify(a));
        assertFalse(t.verify(a, a));
        assertFalse(t.verify(b));
    }
}