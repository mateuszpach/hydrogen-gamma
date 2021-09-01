package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
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

class CountLettersTest {

    @Test
    void executeTiles() {
        // Given
        TextVariable t = new TextVariable(" qwerty!@#\nQWErty123 .  aaaaaa");

        ArgumentCaptor<Tile> captor = ArgumentCaptor.forClass(Tile.class);
        TilesContainer container = Mockito.mock(TilesContainer.class);

        // When
        new CountLetters().execute(container, t);
        Mockito.verify(container).addTile(captor.capture());
        List<Tile> tiles = captor.getAllValues();

        // Then
        assertEquals(1, tiles.size());

        Map<Character, Integer> expectedLettersFreq = Map.of(
                'q', 2,
                'w', 2,
                'e', 2,
                'r', 2,
                't', 2,
                'y', 2,
                'a', 6
        );
        Tile expectedTile = new TableTile(expectedLettersFreq, "Number of letters");
        assertEquals(expectedTile.getContent(), tiles.get(0).getContent());

        assertEquals(expectedTile.getLabel(), tiles.get(0).getLabel());
    }

    @Test
    void verify() {
        TextVariable t = Mockito.mock(TextVariable.class);
        NumericVariable x = Mockito.mock(NumericVariable.class);

        assertTrue(new CountLetters().verify(t));

        assertFalse(new CountLetters().verify(t, t));
        assertFalse(new CountLetters().verify(x));
    }
}