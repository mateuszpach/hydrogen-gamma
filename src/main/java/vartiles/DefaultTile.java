package vartiles;

public abstract class DefaultTile implements Tile {
    protected String label;

    @Override
    public String getLabel() {
        return '$' + label + '$';
    }

    public DefaultTile setLabel(String label) {
        this.label = label;
        return this;
    }
}
