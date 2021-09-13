package hydrogengamma.model.modules.tilefactories;

import hydrogengamma.model.modules.Tile;
import hydrogengamma.model.variables.FunctionVariable;

public interface FunctionTileFactory extends TileFactory {
    Tile getFunctionTile(FunctionVariable content, String label);
}
