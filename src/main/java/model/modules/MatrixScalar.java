package model.modules;

import model.Module;
import model.TilesContainer;
import model.Variable;
import model.variables.MatrixVariable;
import model.variables.NumericVariable;

public enum MatrixScalar implements Module<MatrixVariable> {
    INSTANCE;

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
        for (int i = 0; i < a.rowsNum(); ++i)
            for (int j = 0; j < a.colsNum(); ++j)
                c[i][j] = a.get(i, j) * b.getValue();
        return new MatrixVariable(c);
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 2
                && (args[0] instanceof MatrixVariable && args[1] instanceof NumericVariable
                || args[0] instanceof NumericVariable && args[1] instanceof MatrixVariable);
    }
}