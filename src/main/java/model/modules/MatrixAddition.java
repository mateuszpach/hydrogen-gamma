package model.modules;

import model.Module;
import model.TilesContainer;
import model.Variable;
import model.variables.MatrixVariable;

public class MatrixAddition implements Module<MatrixVariable> {

    @Override
    public MatrixVariable execute(TilesContainer container, Variable<?>... args) {
        MatrixVariable a = (MatrixVariable) args[0];
        MatrixVariable b = (MatrixVariable) args[1];
        double[][] c = new double[a.rowsNum()][a.colsNum()];
        for (int i = 0; i < a.rowsNum(); ++i)
            for (int j = 0; j < a.colsNum(); ++j)
                c[i][j] = a.get(i, j) + b.get(i, j);
        return new MatrixVariable(c);
    }

    @Override
    public boolean verify(Variable<?>... args) {
        if (args.length == 2 && args[0] instanceof MatrixVariable && args[1] instanceof MatrixVariable) {
            MatrixVariable a = (MatrixVariable) args[0];
            MatrixVariable b = (MatrixVariable) args[1];
            return a.colsNum() == b.colsNum() && a.rowsNum() == b.rowsNum();
        }
        return false;
    }
}