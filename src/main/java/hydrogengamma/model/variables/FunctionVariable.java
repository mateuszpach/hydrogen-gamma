package hydrogengamma.model.variables;

import hydrogengamma.model.parsers.standard.Variable;

import java.util.Objects;

public class FunctionVariable implements Variable<String> {

    private final String value;

    public FunctionVariable(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionVariable that = (FunctionVariable) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
