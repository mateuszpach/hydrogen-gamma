package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.modules.tilefactories.NumericTileFactory;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class LevenshteinDistanceTest {

    TilesContainer container = Mockito.mock(TilesContainer.class);
    NumericTileFactory factory = Mockito.mock(NumericTileFactory.class);
    Tile createdTile = Mockito.mock(Tile.class);

    @Test
    void correctResult() {
        TextVariable word1 = new TextVariable("Sunday");
        TextVariable word2 = new TextVariable("Saturday");
        LevenshteinDistance module = new LevenshteinDistance(factory);
        NumericVariable e = new NumericVariable(3);

        NumericVariable result = module.execute(container, word1, word2);

        assertEquals(e, result);
    }

    @Test
    void factoryCommunication() {
        TextVariable word1 = new TextVariable("Sunday");
        TextVariable word2 = new TextVariable("Saturday");
        LevenshteinDistance module = new LevenshteinDistance(factory);

        NumericVariable result = module.execute(container, word1, word2);

        Mockito.verify(factory).getNumericTile(result, "Levenshtein distance of");
        Mockito.verifyNoMoreInteractions(factory);
    }

    @Test
    void containerCommunication() {
        TextVariable word1 = new TextVariable("Sunday");
        TextVariable word2 = new TextVariable("Saturday");
        LevenshteinDistance module = new LevenshteinDistance(factory);
        Mockito.when(factory.getNumericTile(new NumericVariable(3), "Levenshtein distance of")).then((x) -> createdTile);

        module.execute(container, word1, word2);

        Mockito.verify(container).addTile(createdTile);
        Mockito.verifyNoMoreInteractions(container);
    }

    @Test
    public void verifyTest() {
        TextVariable t = Mockito.mock(TextVariable.class);
        NumericVariable x = Mockito.mock(NumericVariable.class);

        assertTrue(new LevenshteinDistance(factory).verify(t, t));

        assertFalse(new LevenshteinDistance(factory).verify(t));
        assertFalse(new LevenshteinDistance(factory).verify(x, t));
    }
}
