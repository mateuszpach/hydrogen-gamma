package hydrogengamma.model.modules;

import hydrogengamma.model.parsers.standard.TilesContainer;
import hydrogengamma.model.parsers.standard.Variable;
import hydrogengamma.model.parsers.standard.computers.Module;
import hydrogengamma.model.variables.MatrixVariable;

public class MatrixIdentity implements Module<MatrixVariable> {
    @Override
    public MatrixVariable execute(TilesContainer container, Variable<?>... args) {
        return new MatrixVariable(((MatrixVariable) args[0]).getValue());
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 1 && args[0] instanceof MatrixVariable;
    }
}
