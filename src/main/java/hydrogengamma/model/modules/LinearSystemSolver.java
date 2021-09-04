package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.utils.LinearAlgebra;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.utils.Pair;
import hydrogengamma.model.modules.tilefactories.MatrixTileFactory;

public class LinearSystemSolver implements Module<MatrixVariable> {

    private final MatrixTileFactory factory;

    public LinearSystemSolver(MatrixTileFactory factory) {
        this.factory = factory;
    }

    @Override
    public MatrixVariable execute(TilesContainer container, Variable<?>... args) {
        MatrixVariable solution = solveLinearSystem((MatrixVariable)args[0], (MatrixVariable) args[1]);
        container.addTile(factory.getMatrixTile(solution, "AX=B solution where A, B are"));
        return solution;
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
