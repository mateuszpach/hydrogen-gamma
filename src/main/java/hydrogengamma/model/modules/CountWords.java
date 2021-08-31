package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.model.variables.VoidVariable;
import hydrogengamma.vartiles.TableTile;

import java.util.Map;
import java.util.TreeMap;

public class CountWords implements Module<VoidVariable> {

    @Override
    public VoidVariable execute(TilesContainer container, Variable<?>... args) {
        String text = ((TextVariable) args[0]).getValue();
        String[] words = text.trim().replaceAll("[^a-zA-Z ]", " ").split("\\s+");
        Map<String, Integer> wordsFreq = new TreeMap<>();
        for (String word : words) {
            Integer oldValue = wordsFreq.getOrDefault(word, 0);
            wordsFreq.put(word, oldValue + 1);
        }
        container.addTile(new TableTile(wordsFreq, "Frequencies of words in"));
        return new VoidVariable();
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 1 && args[0] instanceof TextVariable;
    }
}