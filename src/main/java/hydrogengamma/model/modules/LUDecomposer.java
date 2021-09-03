package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.utils.LinearAlgebra;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.model.variables.VoidVariable;
import hydrogengamma.utils.Pair;
import hydrogengamma.vartiles.MatrixTile;
import hydrogengamma.vartiles.TextTile;

public class LUDecomposer implements Module<VoidVariable> {

    @Override
    public VoidVariable execute(TilesContainer container, Variable<?>... args) {
        Pair<MatrixVariable, MatrixVariable> lu = decompositionLU((MatrixVariable) args[0]);
        if (containsNaNOrInf(lu.first.getValue()) || containsNaNOrInf(lu.second.getValue())) {
            container.addTile(new TextTile(new TextVariable("LU decomposition could not be found"), "LU not found"));
            return new VoidVariable();
        }
        container.addTile(new MatrixTile(lu.first, "L from LU decomposition of"));
        container.addTile(new MatrixTile(lu.second, "U from LU decomposition of"));
        return new VoidVariable();
    }

    // given A finds matrices L and U such that LU = A, L is lower triangular and U is upper triangular
    private static Pair<MatrixVariable, MatrixVariable> decompositionLU(MatrixVariable matrix) {
        int n = matrix.rowsNum();
        int m = matrix.colsNum();
        if (n != m) throw new LinearAlgebra.MatrixNotSquareException();

        Pair<MatrixVariable, MatrixVariable> initialLU = LinearAlgebra.prepareLU(matrix);
        double[][] L = initialLU.first.getValue();
        double[][] U = initialLU.second.getValue();

        for (int i = 0; i < n; i++) {
            LinearAlgebra.performGaussElimStep(L, U, i);
        }

        return new Pair<>(new MatrixVariable(L), new MatrixVariable(U));
    }

    private static boolean containsNaNOrInf(double[][] m) {
        for (double[] doubles : m) {
            for (double aDouble : doubles) {
                if (Double.isInfinite(aDouble) || Double.isNaN(aDouble))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 1 && args[0].getClass() == MatrixVariable.class;
    }
}
