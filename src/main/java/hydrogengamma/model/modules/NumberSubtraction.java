package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.NumericVariable;

public class NumberSubtraction implements Module<NumericVariable> {
    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        NumericVariable a, b;
        a = (NumericVariable) args[0];
        if (args.length == 1)
            return new NumericVariable(-a.getValue());
        else {
            b = (NumericVariable) args[1];
            return new NumericVariable(a.getValue() - b.getValue());
        }
    }

    @Override
    public boolean verify(Variable<?>... args) {
        if (args.length > 2 || args.length == 0)
            return false;
        NumericVariable a;
        try {
            for (Variable<?> arg : args) a = (NumericVariable) arg;
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
