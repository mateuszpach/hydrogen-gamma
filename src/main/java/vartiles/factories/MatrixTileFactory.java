package vartiles.factories;

import model.variables.FunctionVariable;
import model.variables.MatrixVariable;
import vartiles.FunctionTile;
import vartiles.MatrixTile;

public class MatrixTileFactory {
    public MatrixTile get(MatrixVariable variable, String label) {
        MatrixTile tile = new MatrixTile(variable);
        tile.setLabel(label);
        return tile;
    }
}
