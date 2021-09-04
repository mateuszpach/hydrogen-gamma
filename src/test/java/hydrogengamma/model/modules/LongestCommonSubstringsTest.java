package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.modules.tilefactories.TableTileFactory;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.vartiles.TableTile;
import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LongestCommonSubstringsTest {

    private TableTileFactory factory = Mockito.mock(TableTileFactory.class);

    @Test
    void executeTiles() {
        // Given
        TextVariable word1 = new TextVariable("abaabaab");
        TextVariable word2 = new TextVariable("xdabaabxdxdabaab");

        ArgumentCaptor<Tile> captor = ArgumentCaptor.forClass(Tile.class);
        TilesContainer container = Mockito.mock(TilesContainer.class);

        // When
        new LongestCommonSubstrings(factory).execute(container, word1, word2);
        Mockito.verify(container).addTile(captor.capture());
        List<Tile> tiles = captor.getAllValues();

        // Then
        assertEquals(1, tiles.size());

        ArrayList<String> expectedResult = new ArrayList<>(Arrays.asList(
                "abaab",
                "abaab",
                "abaab",
                "abaab"
        ));
        /*Tile expectedTile = new TableTile(expectedResult, "Longest common substrings of");
        assertEquals(expectedTile.getContent(), tiles.get(0).getContent());
        TODO Mateusz

        assertEquals(expectedTile.getLabel(), tiles.get(0).getLabel());*/
    }

    @Test
    void verify() {
        TextVariable t = Mockito.mock(TextVariable.class);
        NumericVariable x = Mockito.mock(NumericVariable.class);

        assertTrue(new LongestCommonSubstrings(factory).verify(t, t));

        assertFalse(new LongestCommonSubstrings(factory).verify(t, x));
        assertFalse(new LongestCommonSubstrings(factory).verify(t));
    }
}