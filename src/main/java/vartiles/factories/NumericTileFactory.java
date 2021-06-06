package vartiles.factories;

import model.variables.FunctionVariable;
import model.variables.NumericVariable;
import vartiles.FunctionTile;
import vartiles.NumericTile;

public class NumericTileFactory {
    public NumericTile get(NumericVariable variable, String label) {
        NumericTile tile = new NumericTile(variable);
        tile.setLabel(label);
        return tile;
    }
}
