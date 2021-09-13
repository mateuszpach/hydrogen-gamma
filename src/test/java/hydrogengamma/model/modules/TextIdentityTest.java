package hydrogengamma.model.modules;

import hydrogengamma.model.parsers.standard.TilesContainer;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.TextVariable;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class TextIdentityTest {
    TilesContainer container = Mockito.mock(TilesContainer.class);

    @Test
    void executeTest() {
        TextVariable f = new TextVariable("x");
        TextIdentity i = new TextIdentity();

        assertEquals(f, i.execute(container, f));
    }

    @Test
    void verifyTest() {
        TextVariable m = new TextVariable("x");
        FunctionVariable f = new FunctionVariable("x");
        TextIdentity i = new TextIdentity();

        assertTrue(i.verify(m));
        assertFalse(i.verify(f));
    }
}