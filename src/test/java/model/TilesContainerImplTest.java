package model;

import model.variables.FunctionVariable;
import model.variables.MatrixVariable;
import org.junit.jupiter.api.Test;
import vartiles.FunctionTile;
import vartiles.MatrixTile;
import vartiles.Tile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TilesContainerImplTest {

    @Test
    public void addTest() {
        MatrixVariable m = new MatrixVariable(new double[][] {{0, 1, 2},{3, 2, 4}});
        Tile mTile = new MatrixTile(m);
        FunctionVariable f = new FunctionVariable("sin(x)");
        Tile fTile = new FunctionTile(f);
        TilesContainer tilesContainer = new TilesContainerImpl();

        tilesContainer.addTile(mTile);
        tilesContainer.addTile(fTile);

        List<Tile> tiles = tilesContainer.getTiles();
        assertEquals(2, tiles.size());
        assertEquals(mTile, tiles.get(0));
        assertEquals(fTile, tiles.get(1));
    }
}