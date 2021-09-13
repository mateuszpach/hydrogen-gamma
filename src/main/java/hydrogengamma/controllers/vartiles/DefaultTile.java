package hydrogengamma.controllers.vartiles;

import hydrogengamma.model.modules.Tile;

public abstract class DefaultTile implements Tile {
    private final String label;

    public DefaultTile(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
