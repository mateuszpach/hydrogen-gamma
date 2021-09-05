package hydrogengamma.model.parsers.standard;

import hydrogengamma.controllers.Computer;
import hydrogengamma.controllers.Expression;
import hydrogengamma.model.*;
import hydrogengamma.utils.Pair;
import hydrogengamma.vartiles.Tile;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StandardComputer implements Computer {
    private static final Logger logger = Logger.getLogger(StandardComputer.class);

    public TilesContainer compute(Map<String, Variable<?>> loaderVariables, List<Expression> expressions) {
        TilesContainer container = new TilesContainerImpl();
        Map<String, Pair<String, Variable<?>>> variables = getVariables(loaderVariables);

        for (Expression exp : expressions) {
            String functionName = exp.getOperationName();
            List<String> subexpressionsIds = exp.getSubExpressions();

            String[] subLabels = new String[subexpressionsIds.size()];
            Variable<?>[] subComponents = getSubComponents(variables, exp, subexpressionsIds, subLabels);
//            Arrays.stream(subComponents).forEach(logger::debug);

            Optional<Modules> matchedModule = getMatchedModule(functionName, subComponents);
            if (matchedModule.isEmpty())
                throw new ParsingException(String.format("Couldn't find possible operation associated with %s to obtain %s as %s\n", functionName, exp.getId(), exp.getLabel()));
            logger.debug("Matched module " + matchedModule.get().name());

            String argsStr = String.join(", ", subLabels);
            TilesContainerDecorator containerDecorator = new TilesContainerDecorator(new TilesContainerImpl(), argsStr);

            Variable<?> value = matchedModule.get().module.execute(containerDecorator, subComponents);
            for (Tile tile : containerDecorator.getTiles())
                container.addTile(tile);
            variables.put(exp.getId(), new Pair<>(exp.getLabel(), value));

        }
        return container;
    }

    @NotNull
    private Map<String, Pair<String, Variable<?>>> getVariables(Map<String, Variable<?>> loaderVariables) {
        Map<String, Pair<String, Variable<?>>> variables = new TreeMap<>();
        for (String key : loaderVariables.keySet()) {
            variables.put(key, new Pair<>(key, loaderVariables.get(key))); // local variables holding both value and label (variable name for "loaded" variables)
        }
        return variables;
    }

    @NotNull
    private Optional<Modules> getMatchedModule(String functionName, Variable<?>[] subComponents) {
        return EnumSet.allOf(Modules.class)
                .stream()
                .filter(x -> x.name.equals(functionName))
                .filter(x -> x.module.verify(subComponents))
                .findFirst();
    }

    @NotNull
    private Variable<?>[] getSubComponents(Map<String, Pair<String, Variable<?>>> variables, Expression exp, List<String> subexpressionsIds, String[] subLabels) {
        Variable<?>[] subComponents = new Variable[subexpressionsIds.size()];

        for (int j = 0; j < subexpressionsIds.size(); ++j) {
            String subId = subexpressionsIds.get(j);
            if (variables.containsKey(subId)) {
                subLabels[j] = variables.get(subId).first;
                subComponents[j] = variables.get(subId).second;
            } else {
                throw new ParsingException("Could not find variable " + subId + " when computing " + exp.getId() + " as " + exp.getLabel() + "\n");
            }
        }
        return subComponents;
    }

}