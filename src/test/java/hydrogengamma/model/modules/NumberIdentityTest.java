package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.NumericVariable;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class NumberIdentityTest {
    TilesContainer container = Mockito.mock(TilesContainer.class);

    @Test
    void executeTest() {
        NumericVariable f = new NumericVariable(1);
        NumberIdentity i = new NumberIdentity();

        assertEquals(f, i.execute(container, f));
    }

    @Test
    void verifyTest() {
        NumericVariable m = new NumericVariable(1);
        FunctionVariable f = new FunctionVariable("x");
        NumberIdentity i = new NumberIdentity();

        assertTrue(i.verify(m));
        assertFalse(i.verify(f));
    }
}