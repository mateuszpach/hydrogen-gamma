package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.TilesContainerImpl;
import hydrogengamma.model.modules.MatrixAddition;
import hydrogengamma.model.modules.MatrixMultiplication;
import hydrogengamma.model.modules.MatrixSubtraction;
import hydrogengamma.model.modules.MatrixTranspose;
import hydrogengamma.model.variables.MatrixVariable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixOperationsTest {
    /*

    TilesContainer container = new TilesContainerImpl();
    MatrixMultiplication mull = new MatrixMultiplication(factory);
    MatrixAddition add = new MatrixAddition(factory);
    MatrixSubtraction sub = new MatrixSubtraction(factory);
    MatrixTranspose tran = new MatrixTranspose(factory);

    @Test
    void correct() {
        MatrixVariable A = new MatrixVariable(new double[][]{{1, -2}, {-3, 4}, {5, -6}});
        MatrixVariable B = new MatrixVariable(new double[][]{{1, -2, 3}, {-4, 5, -6}});
        assertTrue(mull.verify(A, B));
        assertTrue(add.verify(A, A));
        assertTrue(sub.verify(A, A));
        assertTrue(tran.verify(A));
        assertEquals(new MatrixVariable(new double[][]{{9, -12, 15}, {-19, 26, -33}, {29, -40, 51}}), mull.execute(container, A, B));
        assertEquals(new MatrixVariable(new double[][]{{2, -4}, {-6, 8}, {10, -12}}), add.execute(container, A, A));
        assertEquals(new MatrixVariable(new double[][]{{0, 0}, {0, 0}, {0, 0}}), sub.execute(container, A, A));
        assertEquals(new MatrixVariable(new double[][]{{1, -3, 5}, {-2, 4, -6}}), tran.execute(container));
    }

    @Test
    void mismatch() {
        MatrixVariable A = new MatrixVariable(new double[][]{{1, -2, 3}, {-4, 5, -6}});
        MatrixVariable B = new MatrixVariable(new double[][]{{1, -2, 3}, {-4, 5, -6}});
        MatrixVariable C = new MatrixVariable(new double[][]{{1, -2}, {-3, 4}, {5, -6}});
        assertFalse(mull.verify(A, B));
        assertFalse(add.verify(A, C));
        assertFalse(sub.verify(A, C));
        assertFalse(mull.verify(A));
        assertFalse(mull.verify(A, C, A));
        assertFalse(add.verify(A));
        assertFalse(add.verify(A, A, A));
        assertFalse(sub.verify(A));
        assertFalse(sub.verify(A, A, A));
        assertFalse(tran.verify(A, A));
    }
    TODO Michal
    */

}
