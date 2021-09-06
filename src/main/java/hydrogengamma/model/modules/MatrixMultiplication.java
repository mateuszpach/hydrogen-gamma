package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.tilefactories.MatrixTileFactory;
import hydrogengamma.model.modules.utils.ModuleException;
import hydrogengamma.model.variables.MatrixVariable;

public class MatrixMultiplication implements Module<MatrixVariable> {

    private final MatrixTileFactory factory;

    public MatrixMultiplication(MatrixTileFactory factory) {
        this.factory = factory;
    }

    @Override
    public MatrixVariable execute(TilesContainer container, Variable<?>... args) {
        MatrixVariable a = (MatrixVariable) args[0];
        MatrixVariable b = (MatrixVariable) args[1];
        if (a.colsNum() != b.rowsNum() || a.rowsNum() != b.colsNum())
            throw new ModuleException("Mismatched dimensions of matrices for product of " + a + " and " + b);
        double[][] c = new double[a.rowsNum()][b.colsNum()];
        for (int i = 0; i < a.rowsNum(); ++i)
            for (int j = 0; j < b.colsNum(); ++j)
                for (int k = 0; k < a.colsNum(); ++k)
                    c[i][j] += a.get(i, k) * b.get(k, j);
        container.addTile(factory.getMatrixTile(new MatrixVariable(c), "Product of"));
        return new MatrixVariable(c);
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return (args.length == 2 && args[0] instanceof MatrixVariable && args[1] instanceof MatrixVariable);
    }
}