package hydrogengamma.model.parsers.standard.computers;

import hydrogengamma.model.parsers.standard.TilesContainer;
import hydrogengamma.model.parsers.standard.Variable;

public interface Module<T extends Variable<?>> {
    T execute(TilesContainer container, Variable<?>... args);

    boolean verify(Variable<?>... args);
}