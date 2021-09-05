package hydrogengamma.model.modules.tilefactories;

import hydrogengamma.vartiles.Tile;

import java.util.List;

public interface SingleColumnTableTileFactory extends TileFactory {
    Tile getSingleColumnTableTile(List<String> content, String label);
}
