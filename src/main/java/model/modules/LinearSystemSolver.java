package model.modules;

import model.Module;
import model.modules.utils.LinearAlgebra;
import model.variables.MatrixVariable;
import utils.Pair;

public class LinearSystemSolver implements Module<MatrixVariable, MatrixVariable> {

    @Override
    public MatrixVariable execute(MatrixVariable... args) {
        return solveLinearSystem(args[0], args[1]);
    }

    // solves linear system of shape Ax = b, where A is square matrix
    public static MatrixVariable solveLinearSystem(MatrixVariable A, MatrixVariable b) {
        Pair<Pair<MatrixVariable, MatrixVariable>, Integer[]> lu = LinearAlgebra.decompositionLUPivoted(A);
        MatrixVariable L = lu.first.first;
        MatrixVariable U = lu.first.second;
        Integer[] perm = lu.second;
        int n = A.rowsNum();

        double[][] Y = LinearAlgebra.solveSystemLower(L, b, perm).value;
        MatrixVariable yMatrix = new MatrixVariable(Y);
        double[][] ans = LinearAlgebra.solveSystemUpper(U, yMatrix, LinearAlgebra.identityPermutation(n)).value;

        return new MatrixVariable(ans);
    }
}
