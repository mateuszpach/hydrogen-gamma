package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.modules.tilefactories.FunctionTileFactory;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class FunctionBiOperatorTest {

    FunctionTileFactory factory = Mockito.mock(FunctionTileFactory.class);
    TilesContainer container = Mockito.mock(TilesContainer.class);
    Tile createdTile = Mockito.mock(Tile.class);

    @Test
    void executeTest() {
        FunctionBiOperator f = new FunctionBiOperator(factory, "",
                (x, y) -> new FunctionVariable("(" + x.getValue() + "+" + y.getValue() + ")"));

        FunctionVariable g = f.execute(container, new FunctionVariable("(sin(x))"), new FunctionVariable("(x^3)"));

        assertEquals("((sin(x))+(x^3))", g.getValue());
    }

    @Test
    void factoryCommunication() {
        FunctionBiOperator f = new FunctionBiOperator(factory, "",
                (x, y) -> new FunctionVariable("(" + x.getValue() + "+" + y.getValue() + ")"));
        FunctionVariable a = new FunctionVariable("(sin(x))");
        FunctionVariable b = new FunctionVariable("(x^3)");

        FunctionVariable g = f.execute(container, a, b);

        Mockito.verify(factory).getFunctionTile(new FunctionVariable("((sin(x))+(x^3))"), "");
        Mockito.verifyNoMoreInteractions(factory);
    }

    @Test
    void containerCommunication() {
        FunctionBiOperator f = new FunctionBiOperator(factory, "",
                (x, y) -> new FunctionVariable("(" + x.getValue() + "+" + y.getValue() + ")"));
        FunctionVariable a = new FunctionVariable("(sin(x))");
        FunctionVariable b = new FunctionVariable("(x^3)");
        Mockito.when(factory.getFunctionTile(new FunctionVariable("((sin(x))+(x^3))"), "")).then((x) -> createdTile);

        FunctionVariable g = f.execute(container, a, b);

        Mockito.verify(container).addTile(createdTile);
        Mockito.verifyNoMoreInteractions(container);
    }

    @Test
    void verifyTest() {
        FunctionBiOperator f = new FunctionBiOperator(factory, "",
                (x, y) -> new FunctionVariable("(" + x.getValue() + "+" + y.getValue() + ")"));

        assertTrue(f.verify(new FunctionVariable(""), new FunctionVariable("")));
        assertFalse(f.verify(new FunctionVariable("")));
        assertFalse(f.verify(new FunctionVariable(""), new TextVariable("")));
    }
}