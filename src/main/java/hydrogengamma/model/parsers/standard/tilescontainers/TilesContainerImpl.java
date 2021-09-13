package hydrogengamma.model.parsers.standard.tilescontainers;

import hydrogengamma.model.modules.Tile;
import hydrogengamma.model.parsers.standard.TilesContainer;

import java.util.ArrayList;

public class TilesContainerImpl implements TilesContainer {

    private final ArrayList<Tile> tiles = new ArrayList<>();

    public TilesContainerImpl() {
    }

    public TilesContainerImpl(Tile tile) {
        addTile(tile);
    }

    @Override
    public void addTile(Tile tile) {
        tiles.add(tile);
    }

    @Override
    public ArrayList<Tile> getTiles() {
        return tiles;
    }
}
