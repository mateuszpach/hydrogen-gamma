package hydrogengamma.model;

import hydrogengamma.vartiles.Tile;
import hydrogengamma.vartiles.TileDecorator;

import java.util.ArrayList;

public class TilesContainerDecorator implements TilesContainer {
    //TODO: cool we got those, would you mind using them LUKASZ
    private final TilesContainer container; // decoratee
    private final String labelPrefix;

    public TilesContainerDecorator(TilesContainer container, String prefix) {
        this.container = container;
        labelPrefix = prefix;
    }

    public TilesContainerDecorator(TilesContainer container, String prefix, Tile tile) {
        this(container, prefix);
        addTile(tile);
    }

    @Override
    public void addTile(Tile tile) {
        container.addTile(new TileDecorator(tile, labelPrefix));
    }

    @Override
    public ArrayList<Tile> getTiles() {
        return container.getTiles();
    }
}
/*
TItle1:     LU(A+B) : L=
    content1: matrix1
TItle2:     LU(A+B) : U=
    content2:  matrix2

 Sum of numbers: a+b+c , d+e

 computer gives suffix for title aas list of arguments
 */