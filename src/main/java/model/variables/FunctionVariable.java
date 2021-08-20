package model.variables;

import model.Variable;

public class FunctionVariable implements Variable<String> {

    private final String value;

    public FunctionVariable(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
