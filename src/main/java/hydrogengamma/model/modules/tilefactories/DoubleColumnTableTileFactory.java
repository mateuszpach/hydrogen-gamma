package hydrogengamma.model.modules.tilefactories;

import hydrogengamma.utils.Pair;
import hydrogengamma.vartiles.Tile;

import java.util.List;

public interface DoubleColumnTableTileFactory extends TileFactory {
    Tile getDoubleColumnTableTile(List<Pair<String, String>> content, String label);
}
