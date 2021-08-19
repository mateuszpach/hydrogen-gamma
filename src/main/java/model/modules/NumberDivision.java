package model.modules;

import model.Module;
import model.TilesContainer;
import model.Variable;
import model.variables.NumericVariable;

public class NumberDivision implements Module<NumericVariable> {
    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        NumericVariable a, b;
        a = (NumericVariable) args[0];
        if (args.length == 1) {
            return new NumericVariable(1d / a.getValue());
        } else {
            b = (NumericVariable) args[1];
            return new NumericVariable(a.getValue() / b.getValue());
        }
    }

    @Override
    public boolean verify(Variable<?>... args) {// should it not allow for 0 division or throw on zero division???
        if (args.length > 2 || args.length == 0)
            return false;
        NumericVariable a, b;
        double x;
        try {
            a = (NumericVariable) args[0];
            if (args.length == 1) {
                x = 1 / a.getValue();
            } else {
                b = (NumericVariable) args[1];
                x = a.getValue() / b.getValue();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
