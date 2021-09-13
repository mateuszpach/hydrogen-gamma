package hydrogengamma.model.modules.tilefactories;

import hydrogengamma.model.modules.Tile;
import hydrogengamma.model.variables.NumericVariable;

public interface NumericTileFactory extends TileFactory {
    Tile getNumericTile(NumericVariable content, String label);
}
