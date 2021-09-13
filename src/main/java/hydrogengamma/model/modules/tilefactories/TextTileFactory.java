package hydrogengamma.model.modules.tilefactories;

import hydrogengamma.model.modules.Tile;
import hydrogengamma.model.variables.TextVariable;

public interface TextTileFactory extends TileFactory {
    Tile getTextTile(TextVariable content, String label);
}
