package hydrogengamma.model.parsers.standard;

import hydrogengamma.model.Modules;
import hydrogengamma.model.TerminalModules;
import hydrogengamma.model.Variable;
import hydrogengamma.vartiles.InfoTile;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;

public class Computer {

    private static final Logger logger = Logger.getLogger(Computer.class);

    public void compute(State state) {
        int lastVar = state.futureIndex;  // TODO wielkość listy zamiast tego MICHAL
        state.futureIndex = 0;
        for (int i = 0; i < lastVar; ++i) {
            String id = state.getSubstitutionName(i);  // TODO rozważ usunięcie MICHAL/MATEUSZ
            State.Expression expression = state.expressions.get(id);
            String functionName = expression.functionName;
            ArrayList<String> subexpressionsIds = expression.subexpressionsIds;

            Variable<?>[] components = new Variable[subexpressionsIds.size()];
            ArrayList<String> subexpressionsTexts = new ArrayList<>();

            for (int j = 0; j < subexpressionsIds.size(); ++j) {
                String subId = subexpressionsIds.get(j);
                if (state.results.containsKey(subId)) {  // TODO zastąp wyjątkami MATEUSZ/MICHAL
                    components[j] = state.results.get(subId).value;
                    subexpressionsTexts.add(state.results.get(subId).text);
                } else {
                    state.msg = "Could not find variable " + subId + " when computing " + id + "\n";
                    return;
                }
            }

            String expressionText = getExpressionText(functionName, subexpressionsTexts);

            Optional<Modules> matchedModule = EnumSet.allOf(Modules.class)
                    .stream()
                    .filter(x -> x.name.equals(functionName))
                    .filter(x -> x.module.verify(components))
                    .findFirst();
            if (matchedModule.isPresent()) {
                logger.debug("Matched module " + matchedModule.get().name());

                Variable<?> value = matchedModule.get().module.execute(state.container, components);

                state.results.put(id, new State.Result(expressionText, value));
                state.container.addTile(new InfoTile(value.getValue().toString(), expressionText)); // TODO moduły muszą same robić kafle LUKASZ
                continue;
            }

            // TODO void variable. Można robić kafle we wszystkich modułach a terminalne zwracać VoidVariable LUKASZ
            Optional<TerminalModules> matchedTerminalModule = EnumSet.allOf(TerminalModules.class)
                    .stream()
                    .filter(x -> x.name.equals(functionName))
                    .filter(x -> x.module.verify(components))
                    .findFirst();
            if (matchedTerminalModule.isPresent()) {
                matchedTerminalModule.get().module.execute(state.container, components);
                continue;
            }

            state.msg = String.format("Couldn't find possible operation associated with %s to obtain %s \n", // TODO throw exceptino M&M'S
                    functionName,
                    id);
            return;
        }
    }

    private String getExpressionText(String functionName, ArrayList<String> subexpressionsTexts) {
        if (functionName.matches("\\W")) {
            return String.join(functionName, subexpressionsTexts);
        } else {
            return String.format("%s(%s)",
                    functionName,
                    String.join(", ", subexpressionsTexts));
        }
    }
}