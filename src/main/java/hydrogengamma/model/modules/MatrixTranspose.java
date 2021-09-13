package hydrogengamma.model.modules;


import hydrogengamma.model.modules.tilefactories.MatrixTileFactory;
import hydrogengamma.model.parsers.standard.TilesContainer;
import hydrogengamma.model.parsers.standard.Variable;
import hydrogengamma.model.parsers.standard.computers.Module;
import hydrogengamma.model.variables.MatrixVariable;

public class MatrixTranspose implements Module<MatrixVariable> {

    private final MatrixTileFactory factory;

    public MatrixTranspose(MatrixTileFactory factory) {
        this.factory = factory;
    }

    @Override
    public MatrixVariable execute(TilesContainer container, Variable<?>... args) {
        MatrixVariable a = (MatrixVariable) args[0];
        double[][] c = new double[a.colsNum()][a.rowsNum()];
        for (int i = 0; i < a.rowsNum(); ++i)
            for (int j = 0; j < a.colsNum(); ++j)
                c[j][i] = a.get(i, j);
        container.addTile(factory.getMatrixTile(new MatrixVariable(c), "Transpose of"));
        return new MatrixVariable(c);
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return (args.length == 1 && args[0] instanceof MatrixVariable);
    }
}
