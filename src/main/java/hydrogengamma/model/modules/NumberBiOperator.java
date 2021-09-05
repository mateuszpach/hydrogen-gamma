package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.tilefactories.NumericTileFactory;
import hydrogengamma.model.variables.NumericVariable;

import java.util.function.BiFunction;


public class NumberBiOperator implements Module<NumericVariable> {
    private final NumericTileFactory factory;
    private final BiFunction<NumericVariable, NumericVariable, NumericVariable> function;
    private final String label;

    public NumberBiOperator(NumericTileFactory factory, String label, BiFunction<NumericVariable, NumericVariable, NumericVariable> function) {
        this.factory = factory;
        this.label = label;
        this.function = function;
    }

    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        NumericVariable result = this.function.apply((NumericVariable) args[0], (NumericVariable) args[1]);
        container.addTile(factory.getNumericTile(result, label));
        return result;
    }

    @Override
    public boolean verify(Variable<?>... args) {// should it not allow for 0 division or throw on zero division???
        if (args.length != 2)
            return false;
        return (args[0] instanceof NumericVariable && args[1] instanceof NumericVariable);
    }
}
