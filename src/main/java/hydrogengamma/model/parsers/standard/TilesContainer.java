package hydrogengamma.model.parsers.standard;

import hydrogengamma.model.modules.Tile;

import java.util.ArrayList;

public interface TilesContainer {
    void addTile(Tile tile);

    ArrayList<Tile> getTiles();
}
