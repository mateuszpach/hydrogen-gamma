package vartiles;

import model.variables.FunctionVariable;

public class FunctionTileMaker implements TileMaker {

    private FunctionVariable function;

    public FunctionTileMaker(FunctionVariable func) {
        function = func;
    }

    @Override
    public String makeHtml() {
        return null;
    }
}
