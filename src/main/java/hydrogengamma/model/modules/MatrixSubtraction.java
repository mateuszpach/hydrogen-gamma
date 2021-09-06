package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.tilefactories.MatrixTileFactory;
import hydrogengamma.model.modules.utils.ModuleException;
import hydrogengamma.model.variables.MatrixVariable;

public class MatrixSubtraction implements Module<MatrixVariable> {

    private final MatrixTileFactory factory;

    public MatrixSubtraction(MatrixTileFactory factory) {
        this.factory = factory;
    }

    @Override
    public MatrixVariable execute(TilesContainer container, Variable<?>... args) {
        MatrixVariable a = (MatrixVariable) args[0];
        MatrixVariable b = (MatrixVariable) args[1];
        if (a.rowsNum() != b.rowsNum() || a.colsNum() != b.colsNum())
            throw new ModuleException("Mismatched dimensions of matrices for difference of " + a + " and " + b);
        double[][] c = new double[a.rowsNum()][a.colsNum()];
        for (int i = 0; i < a.rowsNum(); ++i)
            for (int j = 0; j < a.colsNum(); ++j)
                c[i][j] = a.get(i, j) - b.get(i, j);
        container.addTile(factory.getMatrixTile(new MatrixVariable(c), "Difference of"));
        return new MatrixVariable(c);
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return (args.length == 2 && args[0] instanceof MatrixVariable && args[1] instanceof MatrixVariable);
    }
}