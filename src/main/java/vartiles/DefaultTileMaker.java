package vartiles;

public abstract class DefaultTileMaker implements TileMaker {
    protected String label;

    @Override
    public String getLabel() {
        return '$' + label + '$';
    }

    public DefaultTileMaker setLabel(String label) {
        this.label = label;
        return this;
    }
}
