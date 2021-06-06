package vartiles.factories;

import model.variables.FunctionVariable;
import vartiles.FunctionTile;

public class FunctionTileFactory {
    public FunctionTile get(FunctionVariable variable) {
        return new FunctionTile(variable);
    }
}
