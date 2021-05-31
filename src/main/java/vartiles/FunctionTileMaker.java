package vartiles;

import model.variables.FunctionVariable;

public class FunctionTileMaker extends DefaultTileMaker {

    private FunctionVariable function;

    public FunctionTileMaker(FunctionVariable func) {
        function = func;
    }

    @Override
    public String getContent() {
        return '$' + function.value + '$'; // TODO: change to proper format
    }
}
