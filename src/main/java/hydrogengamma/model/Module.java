package hydrogengamma.model;

import hydrogengamma.model.modules.tilefactories.TileFactory;

public interface Module<T extends Variable<?>> {
    T execute(TilesContainer container, Variable<?>... args);

    boolean verify(Variable<?>... args);
}