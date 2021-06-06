package model;

import vartiles.Tile;

import java.util.ArrayList;

public class TilesContainerImpl implements TilesContainer {

    private final ArrayList<Tile> tiles = new ArrayList<>();

    @Override
    public void addTile(Tile tile) {
        tiles.add(tile);
    }

    @Override
    public ArrayList<Tile> getTiles() {
        return tiles;
    }
}
