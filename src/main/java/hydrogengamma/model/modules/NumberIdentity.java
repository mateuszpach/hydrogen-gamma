package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.modules.tilefactories.NumericTileFactory;

public class NumberIdentity implements Module<NumericVariable> {
    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        return (NumericVariable) args[0];
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 1 && args[0] instanceof NumericVariable;
    }
}
