package model;

import model.Variable;
import model.TilesContainer;

public interface Module<T extends Variable<?>> {
    T execute(TilesContainer container, Variable<?>... args);

    boolean verify(Variable<?>... args);
}