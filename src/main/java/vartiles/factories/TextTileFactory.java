package vartiles.factories;

import model.variables.FunctionVariable;
import model.variables.TextVariable;
import vartiles.FunctionTile;
import vartiles.TextTile;

public class TextTileFactory {
    public TextTile get(TextVariable variable, String label) {
        TextTile tile = new TextTile(variable);
        tile.setLabel(label);
        return tile;
    }
}
