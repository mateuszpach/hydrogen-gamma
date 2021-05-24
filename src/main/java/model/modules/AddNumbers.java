package model.modules;

import model.Module;
import model.variables.NumericVariable;

public class AddNumbers implements Module<NumericVariable, NumericVariable> {

    @Override
    public NumericVariable execute(NumericVariable... args) {
        System.out.println(args.length);
        double sum = 0d;
        for (NumericVariable arg : args) {
            sum += ((NumericVariable) arg).value;
        }
        return new NumericVariable(sum);
    }
}
