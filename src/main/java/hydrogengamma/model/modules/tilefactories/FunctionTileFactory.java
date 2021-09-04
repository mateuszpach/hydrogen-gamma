package hydrogengamma.model.modules.tilefactories;

import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.vartiles.Tile;

public interface FunctionTileFactory extends TileFactory {
    Tile getFunctionTile(FunctionVariable content, String label);
}
