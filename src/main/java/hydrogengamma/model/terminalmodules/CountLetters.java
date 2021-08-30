package hydrogengamma.model.terminalmodules;

import hydrogengamma.model.TerminalModule;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.vartiles.TableTile;

import java.util.Map;
import java.util.TreeMap;

public class CountLetters implements TerminalModule {

    @Override
    public void execute(TilesContainer container, Variable<?>... args) {
        String text = ((TextVariable) args[0]).getValue();
        text = text.replaceAll("[^a-zA-Z ]", "").toLowerCase();
        Map<Character, Integer> lettersFreq = new TreeMap<>();
        for (char letter : text.toCharArray()) {
            if (letter != ' ') {
                Integer oldValue = lettersFreq.getOrDefault(letter, 0);
                lettersFreq.put(letter, oldValue + 1);
            }
        }
        container.addTile(new TableTile(lettersFreq, "Number of letters"));
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 1 && args[0] instanceof TextVariable;
    }
}