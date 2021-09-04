package hydrogengamma.model.modules.tilefactories;

import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.vartiles.Tile;

public interface MatrixTileFactory extends TileFactory {
    Tile getMatrixTile(MatrixVariable content, String label);
}
