package model.modules;

import model.Module;
import model.TilesContainer;
import model.Variable;
import model.variables.MatrixVariable;

public class MatrixIdentity implements Module<MatrixVariable> {
    @Override
    public MatrixVariable execute(TilesContainer container, Variable<?>... args) {
        return (MatrixVariable) args[0];
    }

    @Override
    public boolean verify(Variable<?>... args) {
        if (args.length < 1)
            return false;
        try {
            MatrixVariable a = (MatrixVariable) args[0];
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
