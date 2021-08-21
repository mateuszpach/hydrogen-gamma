package hydrogengamma.model;

public interface TerminalModule {
    void execute(TilesContainer container, Variable<?>... args);

    boolean verify(Variable<?>... args);
}