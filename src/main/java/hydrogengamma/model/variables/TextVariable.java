package hydrogengamma.model.variables;

import hydrogengamma.model.parsers.standard.Variable;

import java.util.Objects;

public class TextVariable implements Variable<String> {

    private final String value;

    public TextVariable(String value) {
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
        TextVariable that = (TextVariable) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
