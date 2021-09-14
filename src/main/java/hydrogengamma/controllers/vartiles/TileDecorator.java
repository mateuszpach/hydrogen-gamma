package hydrogengamma.controllers.vartiles;

import hydrogengamma.model.modules.Tile;

public class TileDecorator implements Tile {

    private final String labelSuffix;
    private final Tile tile; // decoratee

    public TileDecorator(Tile tile, String suffix) {
        this.labelSuffix = suffix;
        this.tile = tile;
    }

    @Override
    public String getContent() {
        return tile.getContent();
    }

    @Override
    public String getLabel() {
        return String.format("%s: $\\mathit{%s}$", tile.getLabel(), labelSuffix);
    }
}
