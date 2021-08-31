package hydrogengamma.model.parsers.standard;

import hydrogengamma.controllers.Computer;
import hydrogengamma.controllers.Expression;
import hydrogengamma.model.*;
import hydrogengamma.utils.Pair;
import hydrogengamma.vartiles.InfoTile;
import hydrogengamma.vartiles.Tile;
import org.apache.log4j.Logger;

import java.util.*;

public class StandardComputer implements Computer {
    private static final Logger logger = Logger.getLogger(StandardComputer.class);

    public TilesContainer compute(Map<String, Variable<?>> variables, List<Expression> expressions) {
        Map<String, Pair<String, Variable<?>>> myVariables = new TreeMap<>();
        for (String key : variables.keySet()) {
            myVariables.put(key, new Pair<>(key, variables.get(key))); // local variables holding both value and label (variable name for "loaded" variables)
        }
        for (Expression expression : expressions)
            System.out.print("received:" + expression);
        TilesContainer container = new TilesContainerImpl();
        myVariables.forEach((id, result) -> {
            Tile tile = new InfoTile(result.second.getValue().toString(), id);
            container.addTile(tile);

            System.out.println("variable:" + id + " as " + result.first + " : " + result.second.getValue());
        });
        // extractor to computer, loader gives list of pairs (varName, input) and expressions
        // use user input as key in map ???
        //
        //
        //- policzone i niepoliczone : osobno tbh
        //- computer ma coś własnego na results
        for (Expression exp : expressions) {
            System.out.println("Computing: " + exp.id);
            String functionName = exp.operationName;
            List<String> subexpressionsIds = exp.subExpressions;

            Variable<?>[] components = new Variable[subexpressionsIds.size()];
            ArrayList<String> subexpressionsTexts = new ArrayList<>();

            for (int j = 0; j < subexpressionsIds.size(); ++j) {
                String subId = subexpressionsIds.get(j);
                if (myVariables.containsKey(subId)) {
                    components[j] = myVariables.get(subId).second;
                    subexpressionsTexts.add(myVariables.get(subId).first);
                } else {
                    throw new ParsingException("Could not find variable " + subId + " when computing " + exp.id + "\n");
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
                myVariables.put(exp.id, new Pair<>(expressionText, value)); //TODO: expressionName shall be replaced with exp.label (once it's ready) MICHAL
                //state.expressions.get(id).setVariable(expressionText, value);
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
            throw new ParsingException(String.format("Couldn't find possible operation associated with %s to obtain %s \n", functionName, exp.id));
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