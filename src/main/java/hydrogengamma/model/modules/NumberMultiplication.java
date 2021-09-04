package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.modules.tilefactories.NumericTileFactory;

public class NumberMultiplication implements Module<NumericVariable> {

    private final NumericTileFactory factory;

    public NumberMultiplication(NumericTileFactory factory) {
        this.factory = factory;
    }

    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        double prod = 1d;
        for (Variable<?> arg : args) {
            prod *= ((NumericVariable) arg).getValue();
        }
        container.addTile(factory.getNumericTile(new NumericVariable(prod), "Product of"));
        return new NumericVariable(prod);
    }

    @Override
    public boolean verify(Variable<?>... args) {
        for (Variable<?> a : args) {
            try {
                NumericVariable b = (NumericVariable) a;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}