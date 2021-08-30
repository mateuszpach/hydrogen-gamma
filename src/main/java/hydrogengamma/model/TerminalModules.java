package hydrogengamma.model;

public enum TerminalModules {
    DUMMY("", null);

    public final String name;
    public final TerminalModule module;

    TerminalModules(String name, TerminalModule module) {
        this.name = name;
        this.module = module;
    }
}
