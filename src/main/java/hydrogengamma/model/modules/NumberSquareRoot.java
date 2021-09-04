package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.NumericVariable;

import static java.lang.Math.sqrt;

public class NumberSquareRoot implements Module<NumericVariable> {

    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        double prod = ((NumericVariable) args[0]).getValue();
        return new NumericVariable(sqrt(prod));
    }

    @Override
    public boolean verify(Variable<?>... args) {
        if (args.length != 1)
            return false;
        try {
            NumericVariable b = (NumericVariable) args[0];
            if (b.getValue() < 0)
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
