package hydrogengamma;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.utils.LinearAlgebra;
import hydrogengamma.model.terminalmodules.LUDecomposer;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class LUDecomposerTest {

    @Test
    void throwingMatrixNotSquare() {
        double[][] a = {{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}};
        double[][] b = {{1.0, 1.0}};
        double[][] c = {{1.0}, {1.0}};
        TilesContainer container = new TilesContainer() {
            @Override
            public void addTile(Tile tile) { }

            @Override
            public ArrayList<Tile> getTiles() { return null; }
        };

        LUDecomposer lu = new LUDecomposer();

        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> lu.execute(container, new MatrixVariable(a)));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> lu.execute(container, new MatrixVariable(b)));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> lu.execute(container, new MatrixVariable(c)));
    }

    // TODO test L and U stored in kafelki. Commented out are old tests from static LinearAlgebra module.

    /*@Test
    void decompositionLUHardMatrices() {
        double[][] a = {{0.0, 1.0}, {1.0, 2.0}};
        double[][] b = {{Math.PI, Math.PI, Math.PI}, {Math.PI, Math.PI, 2.234}, {0.5 * Math.PI, 1.75, 3.356e7}};
        double[][] c = {{0.0, 0.0}, {Double.MIN_VALUE, Double.MIN_VALUE}};

        assertDoesNotThrow(() -> LinearAlgebra.decompositionLU(new MatrixVariable(a)));
        assertDoesNotThrow(() -> LinearAlgebra.decompositionLU(new MatrixVariable(b)));
        assertDoesNotThrow(() -> LinearAlgebra.decompositionLU(new MatrixVariable(c)));
    }

    @Test
    void properDecompositionsLU() {
        double[][] a = {{1.0, 1.0, 1.0}, {4.0, 3.0, -1.0}, {3.0, 5.0, 3.0}};
        double[][] b = {{1.0, 0.0, 0.0, 0.0}, {0.0, 1.0, 0.0, 0.0}, {0.0, 0.0, 1.0, 0.0}, {0.0, 0.0, 0.0, 1.0}};
        double[][] c = {{2.0, 4.0, 3.0, 5.0}, {-4.0, -7.0, -5.0, -8.0}, {6.0, 8.0, 2.0, 9.0}, {4.0, 9.0, -2.0, 14.0}};

        MatrixVariable expectedL1 = new MatrixVariable(new double[][]{{1.0, 0.0, 0.0}, {4.0, 1.0, 0.0}, {3.0, -2.0, 1.0}});
        MatrixVariable expectedU1 = new MatrixVariable(new double[][]{{1.0, 1.0, 1.0}, {0.0, -1.0, -5.0}, {0.0, 0.0, -10.0}});
        MatrixVariable expectedL2 = new MatrixVariable(b);
        MatrixVariable expectedU2 = new MatrixVariable(b.clone());
        MatrixVariable expectedL3 = new MatrixVariable(new double[][]{{1.0, 0.0, 0.0, 0.0}, {-2.0, 1.0, 0.0, 0.0}, {3.0, -4.0, 1.0, 0.0}, {2.0, 1.0, 3.0, 1.0}});
        MatrixVariable expectedU3 = new MatrixVariable(new double[][]{{2.0, 4.0, 3.0, 5.0}, {0.0, 1.0, 1.0, 2.0}, {0.0, 0.0, -3.0, 2.0}, {0.0, 0.0, 0.0, -4.0}});

        Pair<MatrixVariable, MatrixVariable> lu1 = LinearAlgebra.decompositionLU(new MatrixVariable(a));
        Pair<MatrixVariable, MatrixVariable> lu2 = LinearAlgebra.decompositionLU(new MatrixVariable(b));
        Pair<MatrixVariable, MatrixVariable> lu3 = LinearAlgebra.decompositionLU(new MatrixVariable(c));

        assertEquals(expectedL1, lu1.first);
        assertEquals(expectedU1, lu1.second);
        assertEquals(expectedL2, lu2.first);
        assertEquals(expectedU2, lu2.second);
        assertEquals(expectedL3, lu3.first);
        assertEquals(expectedU3, lu3.second);
    }
    */

    @Test
    public void verifyTest() {
        MatrixVariable m = new MatrixVariable(new double[][]{{0, 1}, {1, 0}});
        Variable<double[][]>[] arr1 = new Variable[]{m};
        Variable<double[][]>[] arr2 = new Variable[]{m, m};
        Variable<String>[] arr3 = new Variable[]{new FunctionVariable("sin(x)")};

        assertTrue(new LUDecomposer().verify(arr1));
        assertFalse(new LUDecomposer().verify(arr2));
        assertFalse(new LUDecomposer().verify(arr3));
    }
}
