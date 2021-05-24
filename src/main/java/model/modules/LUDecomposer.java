package model.modules;

import model.Module;
import model.modules.utils.LinearAlgebra;
import model.variables.MatrixVariable;
import utils.Pair;

import javax.sound.sampled.Line;

public class LUDecomposer implements Module<MatrixVariable, MatrixVariable> {

    @Override
    public MatrixVariable execute(MatrixVariable... args) {
        // in future kafel should be stored
        Pair<MatrixVariable, MatrixVariable> lu = decompositionLU(args[0]);
        return null;
    }

    // given A finds matrices L and U such that LU = A, L is lower triangular and U is upper triangular
    private static Pair<MatrixVariable, MatrixVariable> decompositionLU(MatrixVariable matrix) {
        int n = matrix.rowsNum();
        int m = matrix.colsNum();
        if (n != m) throw new LinearAlgebra.MatrixNotSquareException();

        Pair<MatrixVariable, MatrixVariable> initialLU = LinearAlgebra.prepareLU(matrix);
        double[][] L = initialLU.first.value;
        double[][] U = initialLU.second.value;

        for (int i = 0; i < n; i++) {
            LinearAlgebra.performGaussElimStep(new MatrixVariable(L), new MatrixVariable(U), i);
        }

        return new Pair<MatrixVariable, MatrixVariable>(new MatrixVariable(L), new MatrixVariable(U));
    }
}
