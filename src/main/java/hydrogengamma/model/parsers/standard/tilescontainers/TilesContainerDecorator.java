package hydrogengamma.model.parsers.standard.tilescontainers;

import hydrogengamma.controllers.vartiles.TileDecorator;
import hydrogengamma.model.modules.Tile;
import hydrogengamma.model.parsers.standard.TilesContainer;

import java.util.ArrayList;

public class TilesContainerDecorator implements TilesContainer {
    private final TilesContainer container; // decoratee
    private final String labelSuffix;

    public TilesContainerDecorator(TilesContainer container, String prefix) {
        this.container = container;
        labelSuffix = prefix;
    }

    @Override
    public void addTile(Tile tile) {
        container.addTile(new TileDecorator(tile, labelSuffix));
    }

    @Override
    public ArrayList<Tile> getTiles() {
        return container.getTiles();
    }
}
