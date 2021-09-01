package hydrogengamma.model.variables;

import hydrogengamma.model.Variable;

import java.util.Objects;

public class NumericVariable implements Variable<Double> {

    private final double value;

    public NumericVariable(double value) {
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumericVariable that = (NumericVariable) o;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
