package model.modules;

import model.Module;
import model.Variable;
import model.modules.utils.LinearAlgebra;
import model.variables.MatrixVariable;
import utils.Pair;

import javax.sound.sampled.Line;

public class LUDecomposer implements Module<MatrixVariable> {

    @Override
    public MatrixVariable execute(TilesContainer container, Variable<?>... args) {
        // in future kafel should be stored
        Pair<MatrixVariable, MatrixVariable> lu = decompositionLU((MatrixVariable) args[0]);
        return null;
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

        return new Pair<MatrixVariable, MatrixVariable>(new MatrixVariable(L), new MatrixVariable(U));
    }

    @Override
    public boolean verfiy(Variable<?>... args) {
        //TODO
        return false;
    }
}
