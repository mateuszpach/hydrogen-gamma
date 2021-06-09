package model.modules;

import model.Module;
import model.TilesContainer;
import model.Variable;
import model.modules.utils.LinearAlgebra;
import model.variables.MatrixVariable;
import utils.Pair;
import vartiles.factories.MatrixTileFactory;

import javax.sound.sampled.Line;

public class LUDecomposer implements Module<MatrixVariable> {

    @Override
    public MatrixVariable execute(TilesContainer container, Variable<?>... args) {
        // in future kafel should be stored
        Pair<MatrixVariable, MatrixVariable> lu = decompositionLU((MatrixVariable) args[0]);
        container.addTile(new MatrixTileFactory().get(lu.first, "L"));
        container.addTile(new MatrixTileFactory().get(lu.second, "U"));
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
    public boolean verify(Variable<?>... args) {
        return args.length == 1 && args[0].getClass() == MatrixVariable.class;
    }
}
