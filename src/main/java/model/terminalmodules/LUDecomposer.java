package model.terminalmodules;

import model.Module;
import model.TerminalModule;
import model.TilesContainer;
import model.Variable;
import model.modules.utils.LinearAlgebra;
import model.variables.MatrixVariable;
import utils.Pair;
import vartiles.MatrixTile;

public class LUDecomposer implements TerminalModule {

    @Override
    public void execute(TilesContainer container, Variable<?>... args) {
        Pair<MatrixVariable, MatrixVariable> lu = decompositionLU((MatrixVariable) args[0]);
        MatrixTile tileL = new MatrixTile(lu.first);
        tileL.setLabel("L");
        MatrixTile tileU = new MatrixTile(lu.second);
        tileU.setLabel("U");
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

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 1 && args[0].getClass() == MatrixVariable.class;
    }
}
