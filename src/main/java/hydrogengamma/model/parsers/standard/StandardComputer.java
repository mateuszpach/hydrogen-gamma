package hydrogengamma.model.parsers.standard;

import hydrogengamma.controllers.Computer;
import hydrogengamma.controllers.Expression;
import hydrogengamma.model.Modules;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.TilesContainerImpl;
import hydrogengamma.model.Variable;
import hydrogengamma.utils.Pair;
import hydrogengamma.vartiles.InfoTile;
import hydrogengamma.vartiles.Tile;
import org.apache.log4j.Logger;

import java.util.*;

public class StandardComputer implements Computer {
    private static final Logger logger = Logger.getLogger(StandardComputer.class);

    public TilesContainer compute(Map<String, Variable<?>> variables, List<Expression> expressions) {
        //TODO: not my variable (rename) MICHAL
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
        for (Expression exp : expressions) {
            System.out.println("Computing: " + exp.id);
            String functionName = exp.operationName;
            List<String> subexpressionsIds = exp.subExpressions;

            Variable<?>[] components = new Variable[subexpressionsIds.size()];

            for (int j = 0; j < subexpressionsIds.size(); ++j) {
                String subId = subexpressionsIds.get(j);
                if (myVariables.containsKey(subId)) {
                    components[j] = myVariables.get(subId).second;
                } else {
                    throw new ParsingException("Could not find variable " + subId + " when computing " + exp.id + " as " + exp.label + "\n");
                }
            }


            Optional<Modules> matchedModule = EnumSet.allOf(Modules.class)
                    .stream()
                    .filter(x -> x.name.equals(functionName))
                    .filter(x -> x.module.verify(components))
                    .findFirst();
            if (matchedModule.isPresent()) {
                logger.debug("Matched module " + matchedModule.get().name());

                Variable<?> value = matchedModule.get().module.execute(container, components);
                myVariables.put(exp.id, new Pair<>(exp.label, value));
                container.addTile(new InfoTile(value.getValue().toString(), exp.label)); // TODO moduły muszą same robić kafle LUKASZ
                continue;
            }

            throw new ParsingException(String.format("Couldn't find possible operation associated with %s to obtain %s as %s\n", functionName, exp.id, exp.label));
        }
        return container;
    }

}