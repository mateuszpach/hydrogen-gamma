package hydrogengamma.model;

public interface Module<T extends Variable<?>> {
    T execute(TilesContainer container, Variable<?>... args);

    boolean verify(Variable<?>... args);
}