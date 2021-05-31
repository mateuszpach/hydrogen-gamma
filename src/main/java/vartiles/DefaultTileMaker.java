package vartiles;

public abstract class DefaultTileMaker implements TileMaker {
    String label;

    @Override
    public String getLabel() {
        return '$' + label + '$';
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
