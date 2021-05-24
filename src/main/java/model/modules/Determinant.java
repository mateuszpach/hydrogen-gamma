package model.modules;

import model.Module;
import model.modules.utils.LinearAlgebra;
import model.variables.MatrixVariable;
import model.variables.NumericVariable;
import utils.Pair;

public class Determinant implements Module<NumericVariable, MatrixVariable> {
    @Override
    public NumericVariable execute(MatrixVariable... args) {
        Double det = determinant(args[0]);
        return new NumericVariable(det);
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
