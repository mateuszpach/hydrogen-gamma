package hydrogengamma.model.modules.tilefactories;

import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.vartiles.Tile;

public interface NumericTileFactory extends TileFactory {
    Tile getNumericTile(NumericVariable content, String label);
}
