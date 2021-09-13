package hydrogengamma.model.modules;

import hydrogengamma.model.modules.tilefactories.DoubleColumnTableTileFactory;
import hydrogengamma.model.modules.utils.Collections;
import hydrogengamma.model.parsers.standard.TilesContainer;
import hydrogengamma.model.parsers.standard.Variable;
import hydrogengamma.model.parsers.standard.computers.Module;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.model.variables.VoidVariable;
import hydrogengamma.utils.Pair;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CountWords implements Module<VoidVariable> {

    private final DoubleColumnTableTileFactory factory;

    public CountWords(DoubleColumnTableTileFactory factory) {
        this.factory = factory;
    }

    @Override
    public VoidVariable execute(TilesContainer container, Variable<?>... args) {
        String text = ((TextVariable) args[0]).getValue();
        String[] words = text.trim().replaceAll("[^a-zA-Z ]", " ").split("\\s+");
        Map<String, Integer> wordsFreq = new TreeMap<>();
        for (String word : words) {
            Integer oldValue = wordsFreq.getOrDefault(word, 0);
            wordsFreq.put(word, oldValue + 1);
        }
        List<Pair<String, String>> tileContent = Collections.convertMapToListOfPairsOfStrings(wordsFreq);
        container.addTile(factory.getDoubleColumnTableTile(tileContent, "Frequencies of words in"));
        return new VoidVariable();
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 1 && args[0] instanceof TextVariable;
    }
}