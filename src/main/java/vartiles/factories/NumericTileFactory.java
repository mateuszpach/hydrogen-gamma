package vartiles.factories;

import model.variables.NumericVariable;
import vartiles.NumericTile;

public class NumericTileFactory {
    public NumericTile get(NumericVariable variable) {
        return new NumericTile(variable);
    }
}
