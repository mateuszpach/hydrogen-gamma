package hydrogengamma.model.modules.tilefactories;

import hydrogengamma.model.modules.Tile;
import hydrogengamma.utils.Pair;

import java.util.List;

public interface DoubleColumnTableTileFactory extends TileFactory {
    Tile getDoubleColumnTableTile(List<Pair<String, String>> content, String label);
}
