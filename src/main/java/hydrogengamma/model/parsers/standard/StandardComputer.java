package hydrogengamma.model.parsers.standard;

import hydrogengamma.controllers.Computer;
import hydrogengamma.model.*;
import hydrogengamma.vartiles.InfoTile;
import hydrogengamma.vartiles.Tile;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;

public class StandardComputer implements Computer {

    private static final Logger logger = Logger.getLogger(StandardComputer.class);

    public TilesContainer compute(State state) {
        TilesContainer container = new TilesContainerImpl();
        state.expressions.forEach((id, result) -> {
            if (state.isUserMadeName(id)) {
                Tile tile = new InfoTile(result.getVariable().getValue().toString(), result.text);
                container.addTile(tile);

                System.out.println("variable:" + id + " " + result.getVariable().getValue());
            }
        });
        for (String id : state.getComputationOrder()) {
            System.out.println("Computing: " + id);
            State.Expression expression = state.expressions.get(id);
            String functionName = expression.functionName;
            ArrayList<String> subexpressionsIds = expression.subexpressionsIds;

            Variable<?>[] components = new Variable[subexpressionsIds.size()];
            ArrayList<String> subexpressionsTexts = new ArrayList<>();

            for (int j = 0; j < subexpressionsIds.size(); ++j) {
                String subId = subexpressionsIds.get(j);
                if (state.containsKey(subId)) {
                    components[j] = state.expressions.get(subId).getVariable();
                    subexpressionsTexts.add(state.expressions.get(subId).text);
                } else {
                    throw new ParsingException("Could not find variable " + subId + " when computing " + id + "\n");
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

                Variable<?> value = matchedModule.get().module.execute(container, components);

                state.expressions.get(id).setVariable(expressionText, value);
                container.addTile(new InfoTile(value.getValue().toString(), expressionText)); // TODO moduły muszą same robić kafle LUKASZ
                continue;
            }

            // TODO void variable. Można robić kafle we wszystkich modułach a terminalne zwracać VoidVariable LUKASZ
            Optional<TerminalModules> matchedTerminalModule = EnumSet.allOf(TerminalModules.class)
                    .stream()
                    .filter(x -> x.name.equals(functionName))
                    .filter(x -> x.module.verify(components))
                    .findFirst();
            if (matchedTerminalModule.isPresent()) {
                matchedTerminalModule.get().module.execute(container, components);
                continue;
            }
            throw new ParsingException(String.format("Couldn't find possible operation associated with %s to obtain %s \n", functionName, id));
        }
        return container;
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