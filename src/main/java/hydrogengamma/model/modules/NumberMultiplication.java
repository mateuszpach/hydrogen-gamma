package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.vartiles.NumericTile;

public class NumberMultiplication implements Module<NumericVariable> {

    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        double prod = 1d;
        for (Variable<?> arg : args) {
            prod *= ((NumericVariable) arg).getValue();
        }
        container.addTile(new NumericTile(new NumericVariable(prod), "Numbers product"));
        return new NumericVariable(prod);
    }

    @Override
    public boolean verify(Variable<?>... args) {
        for (Variable<?> a : args) {
            try {
                NumericVariable b = (NumericVariable) a;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}