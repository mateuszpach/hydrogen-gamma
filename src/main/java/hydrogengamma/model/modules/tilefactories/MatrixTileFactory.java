package hydrogengamma.model.modules.tilefactories;

import hydrogengamma.model.modules.Tile;
import hydrogengamma.model.variables.MatrixVariable;

public interface MatrixTileFactory extends TileFactory {
    Tile getMatrixTile(MatrixVariable content, String label);
}
