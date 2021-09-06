package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.tilefactories.NumericTileFactory;
import hydrogengamma.model.variables.NumericVariable;

import java.util.function.Function;


public class NumberUnaryOperator implements Module<NumericVariable> {
    private final NumericTileFactory factory;
    private final Function<NumericVariable, NumericVariable> function;
    private final String label;

    public NumberUnaryOperator(NumericTileFactory factory, String label, Function<NumericVariable, NumericVariable> function) {
        this.factory = factory;
        this.label = label;
        this.function = function;
    }

    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        NumericVariable result = this.function.apply((NumericVariable) args[0]);
        container.addTile(factory.getNumericTile(result, label));
        return result;
    }

    @Override
    public boolean verify(Variable<?>... args) {
        if (args.length != 1)
            return false;
        return (args[0] instanceof NumericVariable);
    }
}
