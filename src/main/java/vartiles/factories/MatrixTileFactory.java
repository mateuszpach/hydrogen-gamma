package vartiles.factories;

import model.variables.MatrixVariable;
import vartiles.MatrixTile;

public class MatrixTileFactory {
    public MatrixTile get(MatrixVariable variable) {
        return new MatrixTile(variable);
    }
}
