package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.modules.tilefactories.NumericTileFactory;

public class NumberDivision implements Module<NumericVariable> {
    private final NumericTileFactory factory;

    public NumberDivision(NumericTileFactory factory) {
        this.factory = factory;
    }

    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        NumericVariable a, b;
        a = (NumericVariable) args[0];
        NumericVariable result;
        if (args.length == 1) {
            result = new NumericVariable(1d / a.getValue());
        } else {
            b = (NumericVariable) args[1];
            result = new NumericVariable(a.getValue() / b.getValue());
        }
        container.addTile(factory.getNumericTile(result, "Division of"));
        return result;
    }

    @Override
    public boolean verify(Variable<?>... args) {// should it not allow for 0 division or throw on zero division???
        if (args.length > 2 || args.length == 0)
            return false;
        NumericVariable a, b;
        double x;
        try {
            a = (NumericVariable) args[0];
            if (args.length == 1) {
                x = 1 / a.getValue();
            } else {
                b = (NumericVariable) args[1];
                x = a.getValue() / b.getValue();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
