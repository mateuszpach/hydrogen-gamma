package hydrogengamma.controllers.vartiles;

import hydrogengamma.model.variables.FunctionVariable;

public class FunctionTile extends DefaultTile {

    private final FunctionVariable function;

    public FunctionTile(FunctionVariable func, String label) {
        super(label);
        function = func;
    }

    @Override
    public String getContent() {
        return '$' + function.getValue() + '$'; // TODO: use latex class LUKASZ
    }
}
