package hydrogengamma.vartiles;

public class TileDecorator implements Tile {

    private final String labelPrefix;
    private final Tile tile; // decoratee

    public TileDecorator(Tile tile, String prefix) {
        this.labelPrefix = prefix;
        this.tile = tile;
    }

    @Override
    public String getContent() {
        return tile.getContent();
    }

    @Override
    public String getLabel() {
        return labelPrefix + " " + tile.getLabel();
    }
}
