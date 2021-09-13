package hydrogengamma.model.modules;

import hydrogengamma.model.parsers.standard.TilesContainer;
import hydrogengamma.model.parsers.standard.Variable;
import hydrogengamma.model.parsers.standard.computers.Module;
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
