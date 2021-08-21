package hydrogengamma.model.variables;

import hydrogengamma.model.Variable;

public class NumericVariable implements Variable<Double> {

    private final double value;

    public NumericVariable(double value) {
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }
}
