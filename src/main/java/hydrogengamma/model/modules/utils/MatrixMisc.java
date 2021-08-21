package hydrogengamma.model.modules.utils;

import hydrogengamma.model.variables.MatrixVariable;

import java.util.function.Function;

public interface MatrixMisc {
    static MatrixVariable matrixApplyLambda(MatrixVariable a, Function<Double, Double> b) {
        double[][] c = new double[a.rowsNum()][a.colsNum()];
        for (int i = 0; i < a.rowsNum(); ++i)
            for (int j = 0; j < a.colsNum(); ++j)
                c[i][j] = b.apply(a.get(i, j));
        return new MatrixVariable(c);
    }
}
