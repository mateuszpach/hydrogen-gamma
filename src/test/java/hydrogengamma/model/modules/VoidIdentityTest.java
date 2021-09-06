package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.VoidVariable;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class VoidIdentityTest {

    TilesContainer container = Mockito.mock(TilesContainer.class);

    @Test
    void executeTest() {
        VoidVariable f = new VoidVariable();
        VoidIdentity i = new VoidIdentity();

        assertEquals(f, i.execute(container, f));
    }

    @Test
    void verifyTest() {
        VoidVariable m = new VoidVariable();
        FunctionVariable f = new FunctionVariable("x");
        VoidIdentity i = new VoidIdentity();

        assertTrue(i.verify(m));
        assertFalse(i.verify(f));
    }
}