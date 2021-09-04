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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CountLettersTest {

    TilesContainer container =  Mockito.mock(TilesContainer.class);
    TableTileFactory factory = Mockito.mock(TableTileFactory.class);

    Tile createdTile = null;

    @Test
    void executeTiles() {
        // Given
        TextVariable t = new TextVariable(" qwerty!@#\nQWErty123 .  aaaaaa");
        String[][] e = new String[][]{{"a", "6"}, {"e", "2"}, {"q", "2"}, {"r", "2"}, {"t", "2"}, {"w", "2"}, {"y", "2"}};
        Mockito.when(factory.getTableTile(e, "Number of letters")).then((x) -> {
            createdTile = Mockito.mock(Tile.class);
            return createdTile;
        });

        // Execute
        new CountLetters(factory).execute(container, t);

        // Then
        Mockito.verify(factory).getTableTile(e, "Number of letters");
        Mockito.verify(container).addTile(createdTile);
        Mockito.verifyNoMoreInteractions(factory, container);
    }

    @Test
    void verify() {
        TextVariable t = Mockito.mock(TextVariable.class);
        NumericVariable x = Mockito.mock(NumericVariable.class);

        assertTrue(new CountLetters(factory).verify(t));

        assertFalse(new CountLetters(factory).verify(t, t));
        assertFalse(new CountLetters(factory).verify(x));
    }
}*/
