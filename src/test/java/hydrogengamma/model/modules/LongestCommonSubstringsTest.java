package hydrogengamma.model.modules;

import hydrogengamma.model.modules.tilefactories.SingleColumnTableTileFactory;
import hydrogengamma.model.parsers.standard.TilesContainer;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.variables.TextVariable;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LongestCommonSubstringsTest {

    TilesContainer container = Mockito.mock(TilesContainer.class);
    SingleColumnTableTileFactory factory = Mockito.mock(SingleColumnTableTileFactory.class);
    Tile createdTile = Mockito.mock(Tile.class);

    @Test
    void factoryCommunication() {
        TextVariable word1 = new TextVariable("abaabaab");
        TextVariable word2 = new TextVariable("xdabaabxdxdabaab");
        LongestCommonSubstrings module = new LongestCommonSubstrings(factory);
        ArrayList<String> e = new ArrayList<>(Arrays.asList(
                "abaab",
                "abaab",
                "abaab",
                "abaab"
        ));

        module.execute(container, word1, word2);

        Mockito.verify(factory).getSingleColumnTableTile(e, "Longest common substrings of");
        Mockito.verifyNoMoreInteractions(factory);
    }

    @Test
    void containerCommunication() {
        TextVariable word1 = new TextVariable("abaabaab");
        TextVariable word2 = new TextVariable("xdabaabxdxdabaab");
        LongestCommonSubstrings module = new LongestCommonSubstrings(factory);
        ArrayList<String> e = new ArrayList<>(Arrays.asList(
                "abaab",
                "abaab",
                "abaab",
                "abaab"
        ));
        Mockito.when(factory.getSingleColumnTableTile(e, "Longest common substrings of")).then((x) -> createdTile);

        module.execute(container, word1, word2);

        Mockito.verify(container).addTile(createdTile);
        Mockito.verifyNoMoreInteractions(container);
    }

    @Test
    void verifyTest() {
        TextVariable t = Mockito.mock(TextVariable.class);
        NumericVariable x = Mockito.mock(NumericVariable.class);

        assertTrue(new LongestCommonSubstrings(factory).verify(t, t));

        assertFalse(new LongestCommonSubstrings(factory).verify(t, x));
        assertFalse(new LongestCommonSubstrings(factory).verify(t));
    }
}