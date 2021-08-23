package hydrogengamma.model;

import hydrogengamma.vartiles.Tile;

import java.util.ArrayList;

public interface TilesContainer {
    void addTile(Tile tile);//TODO: can we have it return itself here? so we can return new TilesContainer().addTile(firstItem); ???

    ArrayList<Tile> getTiles();
}
