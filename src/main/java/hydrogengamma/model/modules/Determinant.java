package hydrogengamma.model.modules;

import hydrogengamma.model.modules.tilefactories.NumericTileFactory;
import hydrogengamma.model.modules.utils.LinearAlgebra;
import hydrogengamma.model.parsers.standard.TilesContainer;
import hydrogengamma.model.parsers.standard.Variable;
import hydrogengamma.model.parsers.standard.computers.Module;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.utils.Pair;

public class Determinant implements Module<NumericVariable> {

    private final NumericTileFactory factory;

    public Determinant(NumericTileFactory factory) {
        this.factory = factory;
    }

    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        MatrixVariable matrix = (MatrixVariable) args[0];
        Double det = determinant(matrix);
        container.addTile(factory.getNumericTile(new NumericVariable(det), "Determinant of"));
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
