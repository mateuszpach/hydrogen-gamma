/*
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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CountWordsTest {

    private TableTileFactory factory;

    @Test
    void executeRegularTiles() {
        // Given
        TextVariable t = new TextVariable("Lorem ipsum lorem ipsum dolor sit amet amet amet");

        ArgumentCaptor<Tile> captor = ArgumentCaptor.forClass(Tile.class);
        TilesContainer container = Mockito.mock(TilesContainer.class);

        // When
        new CountWords(factory).execute(container);
        Mockito.verify(container).addTile(captor.capture());
        List<Tile> tiles = captor.getAllValues();

        // Then
        assertEquals(1, tiles.size());

        Map<String, Integer> expectedLettersFreq = Map.of(
                "Lorem", 1,
                "ipsum", 2,
                "lorem", 1,
                "dolor", 1,
                "sit", 1,
                "amet", 3
        );
       */
/* Tile expectedTile = new TableTile(expectedLettersFreq, "Frequencies of words in");
        assertEquals(expectedTile.getContent(), tiles.get(0).getContent());
        TODO Mateusz
        assertEquals(expectedTile.getLabel(), tiles.get(0).getLabel());*//*

    }

    @Test
    void executeSpacesTiles() {
        // Given
        TextVariable t = new TextVariable(" Lorem    lorem dolor sit        ");

        ArgumentCaptor<Tile> captor = ArgumentCaptor.forClass(Tile.class);
        TilesContainer container = Mockito.mock(TilesContainer.class);

        // When
        new CountWords(factory).execute(container, t);
        Mockito.verify(container).addTile(captor.capture());
        List<Tile> tiles = captor.getAllValues();

        // Then
        assertEquals(1, tiles.size());

        Map<String, Integer> expectedLettersFreq = Map.of(
                "Lorem", 1,
                "lorem", 1,
                "dolor", 1,
                "sit", 1
        );
        */
/*Tile expectedTile = new TableTile(expectedLettersFreq, "Frequencies of words in");
        assertEquals(expectedTile.getContent(), tiles.get(0).getContent());
        TODO Mateusz
        assertEquals(expectedTile.getLabel(), tiles.get(0).getLabel());*//*

    }

    @Test
    void executeSpecialCharsTiles() {
        // Given
        TextVariable t = new TextVariable("Lorem $ ip?sum dolor sit \n amet,  ,, amet!");

        ArgumentCaptor<Tile> captor = ArgumentCaptor.forClass(Tile.class);
        TilesContainer container = Mockito.mock(TilesContainer.class);

        // When
        new CountWords(factory).execute(container);
        Mockito.verify(container).addTile(captor.capture());
        List<Tile> tiles = captor.getAllValues();

        // Then
        assertEquals(1, tiles.size());

        Map<String, Integer> expectedLettersFreq = Map.of(
                "Lorem", 1,
                "ip", 1,
                "sum", 1,
                "dolor", 1,
                "sit", 1,
                "amet", 2
        );
        */
/*Tile expectedTile = new TableTile(expectedLettersFreq, "Frequencies of words in");
        assertEquals(expectedTile.getContent(), tiles.get(0).getContent());
        TODO Mateusz
        assertEquals(expectedTile.getLabel(), tiles.get(0).getLabel());*//*

    }

    @Test
    void verify() {
        TextVariable t = Mockito.mock(TextVariable.class);
        NumericVariable x = Mockito.mock(NumericVariable.class);

        assertTrue(new CountWords(factory).verify(t));

        assertFalse(new CountWords(factory).verify(t, t));
        assertFalse(new CountWords(factory).verify(x));
    }
}*/
