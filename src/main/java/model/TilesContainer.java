package model;

import vartiles.Tile;

import java.util.ArrayList;

public interface TilesContainer {
    void addTile(Tile tile);

    ArrayList<Tile> getTiles(); // maybe it should be split into two interfaces
}
