package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.tilefactories.FunctionTileFactory;
import hydrogengamma.model.variables.FunctionVariable;

import java.util.function.BiFunction;

public class FunctionBiOperator implements Module<FunctionVariable> {
    private final FunctionTileFactory factory;
    private final BiFunction<FunctionVariable, FunctionVariable, FunctionVariable> function;
    private final String label;

    public FunctionBiOperator(FunctionTileFactory factory, String label, BiFunction<FunctionVariable, FunctionVariable, FunctionVariable> function) {
        this.factory = factory;
        this.label = label;
        this.function = function;
    }

    @Override
    public FunctionVariable execute(TilesContainer container, Variable<?>... args) {
        FunctionVariable result = this.function.apply((FunctionVariable) args[0], (FunctionVariable) args[1]);
        container.addTile(factory.getFunctionTile(result, label));
        return result;
    }

    @Override
    public boolean verify(Variable<?>... args) {
        if (args.length != 2)
            return false;
        return (args[0] instanceof FunctionVariable && args[1] instanceof FunctionVariable);
    }
}
