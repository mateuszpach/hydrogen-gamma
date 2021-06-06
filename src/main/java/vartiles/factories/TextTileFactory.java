package vartiles.factories;

import model.variables.TextVariable;
import vartiles.TextTile;

public class TextTileFactory {
    public TextTile get(TextVariable variable) {
        return new TextTile(variable);
    }
}
