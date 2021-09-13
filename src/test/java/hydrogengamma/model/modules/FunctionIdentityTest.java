package hydrogengamma.model.modules;

import hydrogengamma.model.parsers.standard.TilesContainer;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.MatrixVariable;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class FunctionIdentityTest {

    TilesContainer container = Mockito.mock(TilesContainer.class);

    @Test
    void executeTest() {
        FunctionVariable f = new FunctionVariable("x");
        FunctionIdentity i = new FunctionIdentity();

        assertEquals(f, i.execute(container, f));
    }

    @Test
    void verifyTest() {
        FunctionVariable f = new FunctionVariable("x");
        MatrixVariable m = new MatrixVariable(new double[][]{{1}});
        FunctionIdentity i = new FunctionIdentity();

        assertTrue(i.verify(f));
        assertFalse(i.verify(m));
    }
}