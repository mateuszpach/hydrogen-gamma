package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.modules.tilefactories.DoubleColumnTableTileFactory;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.utils.Pair;
import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CountLettersTest {

    TilesContainer container = Mockito.mock(TilesContainer.class);
    DoubleColumnTableTileFactory factory = Mockito.mock(DoubleColumnTableTileFactory.class);
    Tile createdTile = null;

    @Test
    void factoryCommunication() {
        TextVariable t = new TextVariable(" qwerty!@#\nQWErty123 .  aaaaaa");
        CountLetters det = new CountLetters(factory);
        List<Pair<String, String>> e = List.of(
                new Pair<>("a", "6"),
                new Pair<>("e", "2"),
                new Pair<>("q", "2"),
                new Pair<>("r", "2"),
                new Pair<>("t", "2"),
                new Pair<>("w", "2"),
                new Pair<>("y", "2")
        );

        det.execute(container, t);

        Mockito.verify(factory).getDoubleColumnTableTile(e, "Number of letters");
        Mockito.verifyNoMoreInteractions(factory);
    }

    @Test
    void containerCommunication() {
        TextVariable t = new TextVariable(" qwerty!@#\nQWErty123 .  aaaaaa");
        CountLetters det = new CountLetters(factory);
        List<Pair<String, String>> e = List.of(
                new Pair<>("a", "6"),
                new Pair<>("e", "2"),
                new Pair<>("q", "2"),
                new Pair<>("r", "2"),
                new Pair<>("t", "2"),
                new Pair<>("w", "2"),
                new Pair<>("y", "2")
        );
        Mockito.when(factory.getDoubleColumnTableTile(e, "Number of letters")).then((x) -> {
            createdTile = Mockito.mock(Tile.class);
            return createdTile;
        });

        det.execute(container, t);

        Mockito.verify(container).addTile(createdTile);
        Mockito.verifyNoMoreInteractions(container);
    }

    @Test
    void verifyTest() {
        TextVariable t = Mockito.mock(TextVariable.class);
        NumericVariable x = Mockito.mock(NumericVariable.class);

        assertTrue(new CountLetters(factory).verify(t));

        assertFalse(new CountLetters(factory).verify(t, t));
        assertFalse(new CountLetters(factory).verify(x));
    }
}
