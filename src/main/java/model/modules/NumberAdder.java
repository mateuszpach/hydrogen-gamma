package model.modules;

import model.Module;
import model.TilesContainer;
import model.Variable;
import model.variables.NumericVariable;

public class NumberAdder implements Module<NumericVariable> {

    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        System.out.println(args.length);
        double sum = 0d;
        for (Variable<?> arg : args) {
            sum += ((NumericVariable) arg).getValue();
        }
        return new NumericVariable(sum);
    }

    @Override
    public boolean verfiy(Variable<?>... args) {
        //TODO
        return false;
    }
}
