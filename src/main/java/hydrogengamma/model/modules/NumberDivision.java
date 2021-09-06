package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.tilefactories.NumericTileFactory;
import hydrogengamma.model.modules.utils.ModuleException;
import hydrogengamma.model.variables.NumericVariable;

public class NumberDivision implements Module<NumericVariable> {
    private final NumericTileFactory factory;
    private final String label = "Division of";

    public NumberDivision(NumericTileFactory factory) {
        this.factory = factory;
    }

    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        NumericVariable a = (NumericVariable) args[0];
        NumericVariable result;
        if (args.length == 1) {
            if (a.getValue() == 0d)
                throw new ModuleException("Division by zero: " + 1 + " / " + a.getValue());
            result = new NumericVariable(1d / a.getValue());
            container.addTile(factory.getNumericTile(result, label));
        } else {
            NumericVariable b = (NumericVariable) args[1];
            if (b.getValue() == 0d)
                throw new ModuleException("Division by zero: " + a.getValue() + " / " + b.getValue());
            result = new NumericVariable(a.getValue() / b.getValue());
            container.addTile(factory.getNumericTile(result, label));
        }
        return result;
    }

    @Override
    public boolean verify(Variable<?>... args) {
        if (args.length == 0 || args.length > 2)
            return false;
        if (args.length == 2 && !(args[1] instanceof NumericVariable))
            return false;
        return (args[0] instanceof NumericVariable);
    }
}
