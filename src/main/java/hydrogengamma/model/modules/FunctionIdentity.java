package hydrogengamma.model.modules;

import hydrogengamma.model.parsers.standard.TilesContainer;
import hydrogengamma.model.parsers.standard.Variable;
import hydrogengamma.model.parsers.standard.computers.Module;
import hydrogengamma.model.variables.FunctionVariable;

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
