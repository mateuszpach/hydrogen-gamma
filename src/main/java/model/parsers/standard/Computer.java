package model.parsers.standard;

import model.*;
import utils.Pair;
import vartiles.InfoTile;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;

public class Computer {
    // TODO: split each matching to separate methods
    public void compute(State state) {
        int lastVar = state.futureIndex;
        state.futureIndex = 0;
        for (int i = 0; i < lastVar; ++i) {
            String varName = state.getSubstitutionName(i);
            Pair<String, ArrayList<String>> recipe = state.futureVariables.get(varName);

            Variable<?>[] components = new Variable[recipe.second.size()];
            for (int j = 0; j < recipe.second.size(); ++j) {
                String var = recipe.second.get(j);
                if (state.varBoxes.containsKey(var)) {
                    components[j] = state.varBoxes.get(var);
                } else {
                    state.msg = "Could not find variable " + var + "when computing #" + i + "\n";
                    return;
                }
            }

            Optional<Modules> matchedModule = EnumSet.allOf(Modules.class)
                    .stream()
                    .filter(x -> x.name.equals(recipe.first))
                    .filter(x -> x.module.verify(components))
                    .findFirst();
            if (matchedModule.isPresent()) {
                Variable<?> got = matchedModule.get().module.execute(state.container, components);
                state.varBoxes.put(varName, got);
                String result = String.format("%s(%s)=%s\n",
                        recipe.first,
                        String.join(",", recipe.second),
                        got.getValue().toString());

                state.container.addTile(new InfoTile(result, varName));
                continue;
            }

            Optional<TerminalModules> matchedTerminalModule = EnumSet.allOf(TerminalModules.class)
                    .stream()
                    .filter(x -> x.name.equals(recipe.first))
                    .filter(x -> x.module.verify(components))
                    .findFirst();
            if (matchedTerminalModule.isPresent()) {
                matchedTerminalModule.get().module.execute(state.container, components);
                continue;
            }

            state.msg = String.format("Couldn't find possible operation associated with %s to obtain %s \n",
                    recipe.first,
                    varName);
            return;
        }
    }
}
