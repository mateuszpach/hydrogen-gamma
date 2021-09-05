package hydrogengamma.model.modules.tilefactories;

import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.utils.Pair;
import hydrogengamma.vartiles.*;

import java.util.List;

public class AllTilesFactory implements
        FunctionTileFactory,
        MatrixTileFactory,
        NumericTileFactory,
        TextTileFactory,
        SingleColumnTableTileFactory,
        DoubleColumnTableTileFactory,
        MatrixAndTextTileFactory,
        TileFactory {

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
    public Tile getSingleColumnTableTile(List<String> content, String label) {
        return new SingleColumnTableTile(content, label);
    }

    @Override
    public Tile getDoubleColumnTableTile(List<Pair<String, String>> content, String label) {
        return new DoubleColumnTableTile(content, label);
    }
}
