package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.modules.tilefactories.MatrixTileFactory;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class MatrixScalarTest {

    TilesContainer container = Mockito.mock(TilesContainer.class);
    MatrixTileFactory factory = Mockito.mock(MatrixTileFactory.class);
    Tile createdTile = Mockito.mock(Tile.class);

    @Test
    void correctResult() {
        MatrixVariable A = new MatrixVariable(new double[][]{{1, -2, 3}, {-4, 5, -6}});
        NumericVariable s = new NumericVariable(-1);
        MatrixScalar module = new MatrixScalar(factory);
        MatrixVariable e = new MatrixVariable(new double[][]{{-1, 2, -3}, {4, -5, 6}});

        MatrixVariable r1 = module.execute(container, A, s);
        MatrixVariable r2 = module.execute(container, s, A);

        assertEquals(r1, e);
        assertEquals(r2, e);
    }

    @Test
    void factoryCommunication() {
        MatrixVariable A = new MatrixVariable(new double[][]{{1, -2, 3}, {-4, 5, -6}});
        NumericVariable s = new NumericVariable(-1);
        MatrixScalar module = new MatrixScalar(factory);

        MatrixVariable r1 = module.execute(container, A, s);
        MatrixVariable r2 = module.execute(container, s, A);

        Mockito.verify(factory, Mockito.times(2)).getMatrixTile(r1, "Matrix-scalar product of");
        Mockito.verifyNoMoreInteractions(factory);
    }

    @Test
    void containerCommunication() {
        MatrixVariable A = new MatrixVariable(new double[][]{{1, -2, 3}, {-4, 5, -6}});
        NumericVariable s = new NumericVariable(-1);
        MatrixScalar module = new MatrixScalar(factory);
        Mockito.when(factory.getMatrixTile(new MatrixVariable(new double[][]{{-1, 2, -3}, {4, -5, 6}}),
                "Matrix-scalar product of")).then((x) -> createdTile);

        module.execute(container, A, s);

        Mockito.verify(container).addTile(createdTile);
        Mockito.verifyNoMoreInteractions(container);
    }

    @Test
    void verifyTest() {
        MatrixVariable A = Mockito.mock(MatrixVariable.class);
        NumericVariable s = Mockito.mock(NumericVariable.class);

        assertTrue(new MatrixScalar(factory).verify(A, s));
        assertTrue(new MatrixScalar(factory).verify(s, A));

        assertFalse(new MatrixScalar(factory).verify(A));
        assertFalse(new MatrixScalar(factory).verify(A, A, A));
        assertFalse(new MatrixScalar(factory).verify(A, A));
        assertFalse(new MatrixScalar(factory).verify(s, s));
    }
}
