package hydrogengamma.model;

import hydrogengamma.vartiles.Tile;

import java.util.ArrayList;

public class TilesContainerImpl implements TilesContainer {

    private final ArrayList<Tile> tiles = new ArrayList<>();

    public TilesContainerImpl() {
    }

    public TilesContainerImpl(Tile tile) {
        this();
        this.addTile(tile);
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
