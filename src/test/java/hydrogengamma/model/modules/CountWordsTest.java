package hydrogengamma.model.modules;

import hydrogengamma.model.modules.tilefactories.DoubleColumnTableTileFactory;
import hydrogengamma.model.parsers.standard.TilesContainer;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.utils.Pair;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CountWordsTest {

    TilesContainer container = Mockito.mock(TilesContainer.class);
    DoubleColumnTableTileFactory factory = Mockito.mock(DoubleColumnTableTileFactory.class);
    Tile createdTile = Mockito.mock(Tile.class);

    @Test
    void factoryCommunication() {
        TextVariable t1 = new TextVariable(" Lorem    lorem dolor sit        ");
        TextVariable t2 = new TextVariable("Lorem $ ip?sum dolor sit \n amet,  ,, amet!");
        CountWords module = new CountWords(factory);
        List<Pair<String, String>> e1 = List.of(
                new Pair<>("Lorem", "1"),
                new Pair<>("dolor", "1"),
                new Pair<>("lorem", "1"),
                new Pair<>("sit", "1")
        );
        List<Pair<String, String>> e2 = List.of(
                new Pair<>("Lorem", "1"),
                new Pair<>("amet", "2"),
                new Pair<>("dolor", "1"),
                new Pair<>("ip", "1"),
                new Pair<>("sit", "1"),
                new Pair<>("sum", "1")
        );

        module.execute(container, t1);
        module.execute(container, t2);

        InOrder factOrd = Mockito.inOrder(factory);
        factOrd.verify(factory).getDoubleColumnTableTile(e1, "Frequencies of words in");
        factOrd.verify(factory).getDoubleColumnTableTile(e2, "Frequencies of words in");
        Mockito.verifyNoMoreInteractions(factory);
    }

    @Test
    void containerCommunication() {
        TextVariable t = new TextVariable(" Lorem    lorem dolor sit        ");
        CountWords module = new CountWords(factory);
        List<Pair<String, String>> e = List.of(
                new Pair<>("Lorem", "1"),
                new Pair<>("dolor", "1"),
                new Pair<>("lorem", "1"),
                new Pair<>("sit", "1")
        );
        Mockito.when(factory.getDoubleColumnTableTile(e, "Frequencies of words in")).then((x) -> createdTile);

        module.execute(container, t);

        Mockito.verify(container).addTile(createdTile);
        Mockito.verifyNoMoreInteractions(container);
    }

    @Test
    void verifyTest() {
        TextVariable t = Mockito.mock(TextVariable.class);
        NumericVariable x = Mockito.mock(NumericVariable.class);

        assertTrue(new CountWords(factory).verify(t));

        assertFalse(new CountWords(factory).verify(t, t));
        assertFalse(new CountWords(factory).verify(x));
    }
}