package hydrogengamma.model.modules.tilefactories;

import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.vartiles.Tile;

public interface TextTileFactory extends TileFactory {
    Tile getTextTile(TextVariable content, String label);
}
