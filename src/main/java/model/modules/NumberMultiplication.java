package model.modules;

import model.Module;
import model.TilesContainer;
import model.Variable;
import model.variables.NumericVariable;

public class NumberMultiplication implements Module<NumericVariable> {

    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        double sum = 1d;
        for (Variable<?> arg : args) {
            sum *= ((NumericVariable) arg).getValue();
        }
        return new NumericVariable(sum);
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