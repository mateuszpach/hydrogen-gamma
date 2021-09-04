package hydrogengamma.model.modules.tilefactories;

import hydrogengamma.vartiles.Tile;

public interface TableTileFactory extends TileFactory {
    Tile getTableTile(String[][] content, String label);
}
