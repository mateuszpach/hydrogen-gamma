package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.vartiles.MatrixTile;
import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatrixScalarTest {

    @Test
    void executeMatrixByScalarResult() {
        // Given
        MatrixVariable A = new MatrixVariable(new double[][]{{1, -2, 3}, {-4, 5, -6}});
        NumericVariable s = new NumericVariable(-1);

        TilesContainer container = Mockito.mock(TilesContainer.class);

        // When
        MatrixVariable result = new MatrixScalar().execute(container, A, s);

        // Then
        MatrixVariable expectedResult = new MatrixVariable(new double[][]{{-1, 2, -3}, {4, -5, 6}});
        assertEquals(expectedResult, result);
    }

    @Test
    void executeMatrixByScalarTiles() {
        // Given
        MatrixVariable A = new MatrixVariable(new double[][]{{1, -2, 3}, {-4, 5, -6}});
        NumericVariable s = new NumericVariable(-1);

        ArgumentCaptor<Tile> captor = ArgumentCaptor.forClass(Tile.class);
        TilesContainer container = Mockito.mock(TilesContainer.class);

        // When
        new MatrixScalar().execute(container, A, s);
        Mockito.verify(container).addTile(captor.capture());
        List<Tile> tiles = captor.getAllValues();

        // Then
        MatrixVariable expectedResult = new MatrixVariable(new double[][]{{-1, 2, -3}, {4, -5, 6}});

        assertEquals(1, tiles.size());
        assertEquals(tiles.get(0).getContent(), new MatrixTile(expectedResult).getContent());
        assertEquals(tiles.get(0).getLabel(), new MatrixTile(expectedResult).getLabel());
    }

    @Test
    void executeScalarByMatrixResult() {
        // Given
        MatrixVariable A = new MatrixVariable(new double[][]{{1, -2, 3}, {-4, 5, -6}});
        NumericVariable s = new NumericVariable(-1);

        TilesContainer container = Mockito.mock(TilesContainer.class);

        // When
        MatrixVariable result = new MatrixScalar().execute(container, s, A);

        // Then
        MatrixVariable expectedResult = new MatrixVariable(new double[][]{{-1, 2, -3}, {4, -5, 6}});
        assertEquals(expectedResult, result);
    }

    @Test
    void executeScalarByMatrixTiles() {
        // Given
        MatrixVariable A = new MatrixVariable(new double[][]{{1, -2, 3}, {-4, 5, -6}});
        NumericVariable s = new NumericVariable(-1);

        ArgumentCaptor<Tile> captor = ArgumentCaptor.forClass(Tile.class);
        TilesContainer container = Mockito.mock(TilesContainer.class);

        // When
        new MatrixScalar().execute(container, s, A);
        Mockito.verify(container).addTile(captor.capture());
        List<Tile> tiles = captor.getAllValues();

        // Then
        MatrixVariable expectedResult = new MatrixVariable(new double[][]{{-1, 2, -3}, {4, -5, 6}});

        assertEquals(1, tiles.size());
        assertEquals(tiles.get(0).getContent(), new MatrixTile(expectedResult).getContent());
        assertEquals(tiles.get(0).getLabel(), new MatrixTile(expectedResult).getLabel());
    }

    @Test
    void verify() {
        MatrixVariable A = Mockito.mock(MatrixVariable.class);
        NumericVariable s = Mockito.mock(NumericVariable.class);

        assertTrue(new MatrixScalar().verify(A, s));
        assertTrue(new MatrixScalar().verify(s, A));

        assertFalse(new MatrixScalar().verify(A));
        assertFalse(new MatrixScalar().verify(A, A, A));
        assertFalse(new MatrixScalar().verify(A, A));
        assertFalse(new MatrixScalar().verify(s, s));
    }
}