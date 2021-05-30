import static org.junit.jupiter.api.Assertions.*;

import model.modules.Determinant;
import model.modules.TilesContainer;
import model.modules.utils.LinearAlgebra;
import model.variables.MatrixVariable;
import org.junit.jupiter.api.Test;


public class DeterminantTest {

    @Test
    void throwingMatrixNotSquare() {
        double[][] a = {{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}};
        double[][] b = {{1.0, 1.0}};
        double[][] c = {{1.0}, {1.0}};
        TilesContainer container = new TilesContainer() {
            @Override
            public void storeTile() {}
        };


        Determinant det = new Determinant();


        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> det.execute(container, new MatrixVariable(a)));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> det.execute(container, new MatrixVariable(b)));
        assertThrows(LinearAlgebra.MatrixNotSquareException.class, () -> det.execute(container, new MatrixVariable(c)));
    }

    @Test
    void properDeterminant() {
        MatrixVariable A = new MatrixVariable(new double[][] {{3.0, 4.0}, {-1.0, 30.0}});
        MatrixVariable B = new MatrixVariable(new double[][] {{1.0}});
        MatrixVariable C = new MatrixVariable(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        TilesContainer container = new TilesContainer() {
            @Override
            public void storeTile() {}
        };

        Determinant det = new Determinant();

        assertTrue(Math.abs(det.execute(container, A).value - 94.0) < 1e-10);
        assertTrue(Math.abs(det.execute(container, B).value - 1.0) < 1e-10);
        assertTrue(Math.abs(det.execute(container, C).value - 0.0) < 1e-10);
    }
}
