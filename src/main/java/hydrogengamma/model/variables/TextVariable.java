package hydrogengamma.model.variables;

import hydrogengamma.model.Variable;

public class TextVariable implements Variable<String> {

    private final String value;

    public TextVariable(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
