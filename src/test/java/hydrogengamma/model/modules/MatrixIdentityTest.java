package hydrogengamma.model.modules;

import hydrogengamma.model.parsers.standard.TilesContainer;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.MatrixVariable;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class MatrixIdentityTest {
    TilesContainer container = Mockito.mock(TilesContainer.class);

    @Test
    void executeTest() {
        MatrixVariable f = new MatrixVariable(new double[][]{{1}});
        MatrixIdentity i = new MatrixIdentity();

        assertEquals(f, i.execute(container, f));
    }

    @Test
    void verifyTest() {
        MatrixVariable m = new MatrixVariable(new double[][]{{1}});
        FunctionVariable f = new FunctionVariable("x");
        MatrixIdentity i = new MatrixIdentity();

        assertTrue(i.verify(m));
        assertFalse(i.verify(f));
    }
}