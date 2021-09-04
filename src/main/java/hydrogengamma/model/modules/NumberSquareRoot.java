package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.modules.tilefactories.NumericTileFactory;

import static java.lang.Math.sqrt;

public class NumberSquareRoot implements Module<NumericVariable> {

    private final NumericTileFactory factory;

    public NumberSquareRoot(NumericTileFactory factory) {
        this.factory = factory;
    }

    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        double prod = ((NumericVariable) args[0]).getValue();
        container.addTile(factory.getNumericTile(new NumericVariable(sqrt(prod)), "Square root of"));
        return new NumericVariable(sqrt(prod));
    }

    @Override
    public boolean verify(Variable<?>... args) {
        if (args.length != 1)
            return false;
        try {
            NumericVariable b = (NumericVariable) args[0];
            if (b.getValue() < 0)
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
