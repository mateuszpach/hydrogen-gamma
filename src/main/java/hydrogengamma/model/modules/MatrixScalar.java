package hydrogengamma.model.modules;

import hydrogengamma.model.modules.tilefactories.MatrixTileFactory;
import hydrogengamma.model.parsers.standard.TilesContainer;
import hydrogengamma.model.parsers.standard.Variable;
import hydrogengamma.model.parsers.standard.computers.Module;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.model.variables.NumericVariable;

public class MatrixScalar implements Module<MatrixVariable> {

    private final MatrixTileFactory factory;

    public MatrixScalar(MatrixTileFactory factory) {
        this.factory = factory;
    }

    @Override
    public MatrixVariable execute(TilesContainer container, Variable<?>... args) {
        MatrixVariable a;
        NumericVariable b;
        if (args[0] instanceof MatrixVariable) {
            a = (MatrixVariable) args[0];
            b = (NumericVariable) args[1];
        } else {
            a = (MatrixVariable) args[1];
            b = (NumericVariable) args[0];
        }
        double[][] c = new double[a.rowsNum()][a.colsNum()];
        for (int i = 0; i < a.rowsNum(); ++i) {
            for (int j = 0; j < a.colsNum(); ++j) {
                c[i][j] = a.get(i, j) * b.getValue();
            }
        }

        MatrixVariable result = new MatrixVariable(c);
        container.addTile(factory.getMatrixTile(result, "Matrix-scalar product of"));
        return result;
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 2
                && (args[0] instanceof MatrixVariable && args[1] instanceof NumericVariable
                || args[0] instanceof NumericVariable && args[1] instanceof MatrixVariable);
    }
}