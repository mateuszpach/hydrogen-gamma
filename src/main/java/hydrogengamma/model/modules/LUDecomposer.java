package hydrogengamma.model.modules;

import hydrogengamma.model.modules.tilefactories.MatrixAndTextTileFactory;
import hydrogengamma.model.modules.utils.LinearAlgebra;
import hydrogengamma.model.modules.utils.ModuleException;
import hydrogengamma.model.parsers.standard.TilesContainer;
import hydrogengamma.model.parsers.standard.Variable;
import hydrogengamma.model.parsers.standard.computers.Module;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.model.variables.VoidVariable;
import hydrogengamma.utils.Pair;

public class LUDecomposer implements Module<VoidVariable> {

    private final MatrixAndTextTileFactory factory;

    public LUDecomposer(MatrixAndTextTileFactory factory) {
        this.factory = factory;
    }

    @Override
    public VoidVariable execute(TilesContainer container, Variable<?>... args) {
        Pair<MatrixVariable, MatrixVariable> lu = decompositionLU((MatrixVariable) args[0]);
        if (containsNaNOrInf(lu.first.getValue()) || containsNaNOrInf(lu.second.getValue())) {
            container.addTile(factory.getTextTile(new TextVariable("LU decomposition could not be found"), "LU not found"));
            return new VoidVariable();
        }
        container.addTile(factory.getMatrixTile(lu.first, "L from LU decomposition of"));
        container.addTile(factory.getMatrixTile(lu.second, "U from LU decomposition of"));
        return new VoidVariable();
    }

    // given A finds matrices L and U such that LU = A, L is lower triangular and U is upper triangular
    private static Pair<MatrixVariable, MatrixVariable> decompositionLU(MatrixVariable matrix) {
        int n = matrix.rowsNum();
        int m = matrix.colsNum();
        if (n != m) throw new ModuleException("Matrix is not square, but an operation requires it to be so");

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
