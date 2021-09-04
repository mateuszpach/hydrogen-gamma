/*
package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.modules.tilefactories.NumericTileFactory;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.vartiles.NumericTile;
import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevenshteinDistanceTest {

    private NumericTileFactory factory;

    @Test
    void executeResult() {
        // Given
        TextVariable word1 = new TextVariable("Sunday");
        TextVariable word2 = new TextVariable("Saturday");

        TilesContainer container = Mockito.mock(TilesContainer.class);

        // When
        NumericVariable result = new LevenshteinDistance(factory).execute(container, word1, word2);

        // Then
        NumericVariable expectedResult = new NumericVariable(3);
        assertEquals(expectedResult, result);
    }

    @Test
    void executeTiles() {
        // Given
        TextVariable word1 = new TextVariable("Sunday");
        TextVariable word2 = new TextVariable("Saturday");

        ArgumentCaptor<Tile> captor = ArgumentCaptor.forClass(Tile.class);
        TilesContainer container = Mockito.mock(TilesContainer.class);

        // When
        new LevenshteinDistance(factory).execute(container, word1, word2);
        Mockito.verify(container).addTile(captor.capture());
        List<Tile> tiles = captor.getAllValues();

        // Then
        assertEquals(1, tiles.size());

        NumericVariable expectedResult = new NumericVariable(3);
        Tile expectedTile = new NumericTile(expectedResult, "Levenshtein distance of");
        assertEquals(expectedTile.getContent(), tiles.get(0).getContent());

        assertEquals(expectedTile.getLabel(), tiles.get(0).getLabel());
    }

    @Test
    void verify() {
        TextVariable t = Mockito.mock(TextVariable.class);
        NumericVariable x = Mockito.mock(NumericVariable.class);

        assertTrue(new LevenshteinDistance(factory).verify(t, t));

        assertFalse(new LevenshteinDistance(factory).verify(t));
        assertFalse(new LevenshteinDistance(factory).verify(x, t));
    }
}*/
