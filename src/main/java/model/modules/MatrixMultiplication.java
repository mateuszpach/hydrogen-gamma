package model.modules;

import model.Module;
import model.TilesContainer;
import model.Variable;
import model.variables.MatrixVariable;

public class MatrixMultiplication implements Module<MatrixVariable> {

    @Override
    public MatrixVariable execute(TilesContainer container, Variable<?>... args) {
        MatrixVariable a = (MatrixVariable) args[0];
        MatrixVariable b = (MatrixVariable) args[1];
        double[][] c = new double[a.rowsNum()][b.colsNum()];
        for (int i = 0; i < a.rowsNum(); ++i)
            for (int j = 0; j < b.colsNum(); ++j)
                for (int k = 0; k < a.colsNum(); ++k)
                    c[i][j] += a.get(i, k) * b.get(k, j);
        return new MatrixVariable(c);
    }

    @Override
    public boolean verify(Variable<?>... args) {
        if (args.length == 2 && args[0] instanceof MatrixVariable && args[1] instanceof MatrixVariable) {
            MatrixVariable a = (MatrixVariable) args[0];
            MatrixVariable b = (MatrixVariable) args[1];
            return a.colsNum() == b.rowsNum();
        }
        return false;
    }
}