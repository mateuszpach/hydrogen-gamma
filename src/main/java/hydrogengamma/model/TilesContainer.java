package hydrogengamma.model;

import hydrogengamma.vartiles.Tile;

import java.util.ArrayList;

public interface TilesContainer {
    void addTile(Tile tile);

    ArrayList<Tile> getTiles();
}
