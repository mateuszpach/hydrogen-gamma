package model.modules;

import model.Module;
import model.TilesContainer;
import model.Variable;
import model.variables.NumericVariable;

public class NumericIdentity implements Module<NumericVariable> {
    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        return (NumericVariable) args[0];
    }

    @Override
    public boolean verify(Variable<?>... args) {
        if (args.length < 1)
            return false;
        try {
            NumericVariable a = (NumericVariable) args[0];
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
