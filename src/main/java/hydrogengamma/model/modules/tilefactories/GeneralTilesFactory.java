package hydrogengamma.model.modules.tilefactories;

import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.vartiles.*;

public class GeneralTilesFactory implements FunctionTileFactory, MatrixTileFactory, NumericTileFactory, TextTileFactory, TableTileFactory, MatrixAndTextTileFactory, TileFactory {
    @Override
    public Tile getFunctionTile(FunctionVariable content, String label) {
        return new FunctionTile(content, label);
    }

    @Override
    public Tile getMatrixTile(MatrixVariable content, String label) {
        return new MatrixTile(content, label);
    }

    @Override
    public Tile getNumericTile(NumericVariable content, String label) {
        return new NumericTile(content, label);
    }

    @Override
    public Tile getTextTile(TextVariable content, String label) {
        return new TextTile(content, label);
    }

    @Override
    public Tile getTableTile(String[][] content, String label) {
        return new TableTile(content, label);
    }
}
