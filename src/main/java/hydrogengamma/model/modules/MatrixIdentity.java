package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.MatrixVariable;

public class MatrixIdentity implements Module<MatrixVariable> {
    @Override
    public MatrixVariable execute(TilesContainer container, Variable<?>... args) {
        return (MatrixVariable) args[0];
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 1 && args[0] instanceof MatrixVariable;
    }
}
