package hydrogengamma.vartiles;

public class TileDecorator implements Tile {

    private final String labelSuffix;
    private final Tile tile; // decoratee

    public TileDecorator(Tile tile, String prefix) {
        this.labelSuffix = prefix;
        this.tile = tile;
    }

    @Override
    public String getContent() {
        return tile.getContent();
    }

    @Override
    public String getLabel() {
//        return String.format("$\\text{%s: } %s$", tile.getLabel(), labelSuffix);
        return String.format("%s: $\\mathit{%s}$", tile.getLabel(), labelSuffix);
    }
}
