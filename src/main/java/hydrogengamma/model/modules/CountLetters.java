package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.tilefactories.DoubleColumnTableTileFactory;
import hydrogengamma.model.modules.utils.Collections;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.model.variables.VoidVariable;
import hydrogengamma.utils.Pair;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CountLetters implements Module<VoidVariable> {

    private final DoubleColumnTableTileFactory factory;

    public CountLetters(DoubleColumnTableTileFactory factory) {
        this.factory = factory;
    }

    @Override
    public VoidVariable execute(TilesContainer container, Variable<?>... args) {
        String text = ((TextVariable) args[0]).getValue();
        text = text.replaceAll("[^a-zA-Z ]", "").toLowerCase();
        Map<Character, Integer> lettersFreq = new TreeMap<>();
        for (char letter : text.toCharArray()) {
            if (letter != ' ') {
                Integer oldValue = lettersFreq.getOrDefault(letter, 0);
                lettersFreq.put(letter, oldValue + 1);
            }
        }
        List<Pair<String, String>> tileContent = Collections.convertMapToListOfPairsOfStrings(lettersFreq);
        container.addTile(factory.getDoubleColumnTableTile(tileContent, "Number of letters"));
        return new VoidVariable();
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 1 && args[0] instanceof TextVariable;
    }
}