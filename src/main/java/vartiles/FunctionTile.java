package vartiles;

import model.variables.FunctionVariable;

public class FunctionTile extends DefaultTile {

    private FunctionVariable function;

    public FunctionTile(FunctionVariable func) {
        function = func;
    }

    @Override
    public String getContent() {
        return '$' + function.getValue() + '$'; // TODO: change to proper format
    }
}
