import model.modules.MatrixOperations;
import org.junit.jupiter.api.Test;
import model.variables.MatrixVariable;
import model.variables.NumericVariable;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixOperationsTest {

    @Test
    void multiplication() {
        MatrixVariable A = new MatrixVariable(new double[][]{{1, -2}, {-3, 4}, {5, -6}});
        MatrixVariable B = new MatrixVariable(new double[][]{{1, -2, 3}, {-4, 5, -6}});
        assertEquals(new MatrixVariable(new double[][]{{9, -12, 15}, {-19, 26, -33}, {29, -40, 51}}),
                MatrixOperations.matrixMultiplication(A, B));
    }

    @Test
    void multiplicationMismatch() {
        MatrixVariable A = new MatrixVariable(new double[][]{{1, -2, 3}, {-4, 5, -6}});
        MatrixVariable B = new MatrixVariable(new double[][]{{1, -2, 3}, {-4, 5, -6}});
        assertThrows(IllegalArgumentException.class,
                () -> MatrixOperations.matrixMultiplication(A, B));
    }

    @Test
    void addition() {
        MatrixVariable A = new MatrixVariable(new double[][]{{1, -2}, {-3, 4}, {5, -6}});
        MatrixVariable B = new MatrixVariable(new double[][]{{1, -2}, {3, -4}, {5, -6}});
        assertEquals(new MatrixVariable(new double[][]{{2, -4}, {0, 0}, {10, -12}}),
                MatrixOperations.matrixAddition(A, B));
    }

    @Test
    void additionMismatch() {
        MatrixVariable A = new MatrixVariable(new double[][]{{1, -2}, {-3, 4}});
        MatrixVariable B = new MatrixVariable(new double[][]{{1, -2, 3}, {-4, 5, -6}});
        assertThrows(IllegalArgumentException.class,
                () -> MatrixOperations.matrixAddition(A, B));
    }

    @Test
    void scalar() {
        MatrixVariable A = new MatrixVariable(new double[][]{{1, -2, 3}, {-4, 5, -6}});
        NumericVariable s = new NumericVariable(-1);
        assertEquals(new MatrixVariable(new double[][]{{-1, 2, -3}, {4, -5, 6}}),
                MatrixOperations.matrixScalar(A, s));
        assertEquals(MatrixOperations.matrixScalar(A, s),
                MatrixOperations.matrixScalar(s, A));
    }

    @Test
    void lambda() {
        MatrixVariable A = new MatrixVariable(new double[][]{{1, -2, 3}, {-4, 5, -6}});
        assertEquals(new MatrixVariable(new double[][]{{4, -2, 8}, {-6, 12, -10}}),
                MatrixOperations.matrixApplyLambda(A, (x) -> 2 * x + 2));
    }

}
