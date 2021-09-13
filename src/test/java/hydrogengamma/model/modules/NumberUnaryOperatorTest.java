package hydrogengamma.model.modules;

import hydrogengamma.model.modules.tilefactories.NumericTileFactory;
import hydrogengamma.model.modules.utils.ModuleException;
import hydrogengamma.model.parsers.standard.TilesContainer;
import hydrogengamma.model.parsers.standard.Variable;
import hydrogengamma.model.variables.NumericVariable;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class NumberUnaryOperatorTest {
    TilesContainer container = Mockito.mock(TilesContainer.class);
    NumericTileFactory factory = Mockito.mock(NumericTileFactory.class);
    Tile createdTile = Mockito.mock(Tile.class);

    @Test
    void canBeThrowing() {
        NumberUnaryOperator throwing = new NumberUnaryOperator(factory, "throw", (a) -> {
            if (a.getValue().equals(0d))
                throw new ModuleException("throwing for no good reason");
            return new NumericVariable(a.getValue() + 2d);
        });

        assertThrows(ModuleException.class, () -> throwing.execute(container, new NumericVariable(0)));
        Mockito.verifyNoInteractions(container, factory);
    }

    @Test
    void correctResult() {
        NumberUnaryOperator add = new NumberUnaryOperator(factory, "+", (a) -> new NumericVariable(a.getValue() + 2));
        assertEquals(new NumericVariable(5d), add.execute(container, new NumericVariable(3)));
    }

    @Test
    void factoryCommunication() {
        NumberUnaryOperator add = new NumberUnaryOperator(factory, "+", (a) -> new NumericVariable(a.getValue() + 2));

        NumericVariable w1 = add.execute(container, new NumericVariable(1));

        InOrder factOrd = Mockito.inOrder(factory);
        factOrd.verify(factory).getNumericTile(w1, "+");
        Mockito.verifyNoMoreInteractions(factory);
    }

    @Test
    void containerCommunication() {
        NumberUnaryOperator add = new NumberUnaryOperator(factory, "+", (a) -> new NumericVariable(a.getValue() + 2));
        Mockito.when(factory.getNumericTile(new NumericVariable(3.0), "+")).then((x) -> createdTile);

        add.execute(container, new NumericVariable(1));

        Mockito.verify(container).addTile(createdTile);
        Mockito.verifyNoMoreInteractions(container);
    }

    @Test
    public void verifyTest() {
        NumberUnaryOperator add = new NumberUnaryOperator(factory, "+", (a) -> new NumericVariable(a.getValue() + 2));
        NumericVariable m = new NumericVariable(1d);
        Variable<?>[] arr1 = new Variable[]{m};
        Variable<?>[] arr2 = new Variable[]{m, m};
        Variable<?>[] arr3 = new Variable[]{(Variable<Double>) () -> 1d};

        assertTrue(add.verify(arr1));
        add = new NumberUnaryOperator(factory, "+", (a) -> new NumericVariable(a.getValue() + 2));
        assertFalse(add.verify(arr2));
        add = new NumberUnaryOperator(factory, "+", (a) -> new NumericVariable(a.getValue() + 2));
        assertFalse(add.verify(arr3));
    }

}
