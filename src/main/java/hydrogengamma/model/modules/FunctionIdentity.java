package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.modules.tilefactories.TileFactory;

public class FunctionIdentity implements Module<FunctionVariable> {
    @Override
    public FunctionVariable execute(TilesContainer container, Variable<?>... args) {
        return (FunctionVariable) args[0];
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 1 && args[0] instanceof FunctionVariable;
    }
}
