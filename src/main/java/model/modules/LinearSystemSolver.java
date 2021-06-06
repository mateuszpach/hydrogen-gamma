package model.modules;

import model.Module;
import model.TilesContainer;
import model.Variable;
import model.modules.utils.LinearAlgebra;
import model.variables.MatrixVariable;
import utils.Pair;
import vartiles.MatrixTile;
import vartiles.factories.MatrixTileFactory;

public class LinearSystemSolver implements Module<MatrixVariable> {

    @Override
    public MatrixVariable execute(TilesContainer container, Variable<?>... args) {
        MatrixVariable sol = solveLinearSystem((MatrixVariable)args[0], (MatrixVariable) args[1]);
        container.addTile(new MatrixTileFactory().get(sol, "linear system solution"));
        return sol;
    }

    // solves linear system of shape Ax = b, where A is square matrix
    public static MatrixVariable solveLinearSystem(MatrixVariable A, MatrixVariable b) {
        Pair<Pair<MatrixVariable, MatrixVariable>, Integer[]> lu = LinearAlgebra.decompositionLUPivoted(A);
        MatrixVariable L = lu.first.first;
        MatrixVariable U = lu.first.second;
        Integer[] perm = lu.second;
        int n = A.rowsNum();

        double[][] Y = LinearAlgebra.solveSystemLower(L, b, perm).getValue();
        MatrixVariable yMatrix = new MatrixVariable(Y);
        double[][] ans = LinearAlgebra.solveSystemUpper(U, yMatrix, LinearAlgebra.identityPermutation(n)).getValue();

        return new MatrixVariable(ans);
    }

    @Override
    public boolean verfiy(Variable<?>... args) {
        return args.length == 2 && args[0].getClass() == MatrixVariable.class && args[1].getClass() == MatrixVariable.class;
    }
}
