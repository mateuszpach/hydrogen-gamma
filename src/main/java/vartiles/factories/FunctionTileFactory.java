package vartiles.factories;

import model.variables.FunctionVariable;
import vartiles.FunctionTile;

public class FunctionTileFactory {
    public FunctionTile get(FunctionVariable variable, String label) {
        FunctionTile tile = new FunctionTile(variable);
        tile.setLabel(label);
        return tile;
    }
}
