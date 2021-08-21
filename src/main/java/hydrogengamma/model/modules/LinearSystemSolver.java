package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.utils.LinearAlgebra;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.utils.Pair;

public class LinearSystemSolver implements Module<MatrixVariable> {

    @Override
    public MatrixVariable execute(TilesContainer container, Variable<?>... args) {
        return solveLinearSystem((MatrixVariable)args[0], (MatrixVariable) args[1]);
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
    public boolean verify(Variable<?>... args) {
        return args.length == 2 && args[0].getClass() == MatrixVariable.class && args[1].getClass() == MatrixVariable.class;
    }
}
