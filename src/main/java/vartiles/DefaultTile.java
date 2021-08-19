package vartiles;

public abstract class DefaultTile implements Tile {
    protected String label;

    @Override
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void addPrefixToLabel(String prefix) {
        this.label = prefix + this.label;
    }
}
