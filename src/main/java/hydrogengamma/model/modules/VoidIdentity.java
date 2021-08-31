package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.VoidVariable;

public class VoidIdentity implements Module<VoidVariable> {
    @Override
    public VoidVariable execute(TilesContainer container, Variable<?>... args) {
        return (VoidVariable) args[0];
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 1 && args[0] instanceof VoidVariable;
    }
}
