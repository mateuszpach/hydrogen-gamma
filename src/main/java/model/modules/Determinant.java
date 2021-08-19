package model.modules;

import model.Module;
import model.TilesContainer;
import model.Variable;
import model.modules.utils.LinearAlgebra;
import model.variables.MatrixVariable;
import model.variables.NumericVariable;
import utils.Pair;
import vartiles.factories.NumericTileFactory;

public class Determinant implements Module<NumericVariable> {
    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        MatrixVariable matrix = (MatrixVariable) args[0];
        Double det = determinant(matrix);
        return new NumericVariable(det);
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 1 && args[0].getClass() == MatrixVariable.class;
    }

    private Double determinant(MatrixVariable A) {
        Pair<Pair<MatrixVariable, MatrixVariable>, Integer[]> lu = LinearAlgebra.decompositionLUPivoted(A);
        MatrixVariable U = lu.first.second;
        double det = 1.0;
        for (int i = 0; i < U.rowsNum(); i++) {
            det *= U.get(i, i);
        }
        return det;
    }
}
