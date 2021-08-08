package model.terminalmodules;

import model.TerminalModule;
import model.TilesContainer;
import model.Variable;
import model.variables.TextVariable;
import vartiles.TableTile;

import java.util.Map;
import java.util.TreeMap;

public class CountWords implements TerminalModule {

    @Override
    public void execute(TilesContainer container, Variable<?>... args) {
        String text = ((TextVariable) args[0]).getValue();
        String[] words = text.trim().replaceAll("[^a-zA-Z ]", " ").split("\\s+");
        Map<String, Integer> wordsFreq = new TreeMap<>();
        for (String word : words) {
            Integer oldValue = wordsFreq.getOrDefault(word, 0);
            wordsFreq.put(word, oldValue + 1);
        }
        container.addTile(new TableTile(wordsFreq));
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 1 && args[0] instanceof TextVariable;
    }
}
