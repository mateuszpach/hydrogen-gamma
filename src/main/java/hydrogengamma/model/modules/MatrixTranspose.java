package hydrogengamma.model.modules;


import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.vartiles.MatrixTile;

public class MatrixTranspose implements Module<MatrixVariable> {

    @Override
    public MatrixVariable execute(TilesContainer container, Variable<?>... args) {
        MatrixVariable a = (MatrixVariable) args[0];
        double[][] c = new double[a.colsNum()][a.rowsNum()];
        for (int i = 0; i < a.rowsNum(); ++i)
            for (int j = 0; j < a.colsNum(); ++j)
                c[j][i] = a.get(i, j);
        container.addTile(new MatrixTile(new MatrixVariable(c), "Transpose of"));
        return new MatrixVariable(c);
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return (args.length == 1 && args[0] instanceof MatrixVariable);
    }
}
