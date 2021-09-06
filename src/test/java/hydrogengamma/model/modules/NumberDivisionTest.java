package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.tilefactories.NumericTileFactory;
import hydrogengamma.model.modules.utils.ModuleException;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class NumberDivisionTest {
    TilesContainer container = Mockito.mock(TilesContainer.class);
    NumericTileFactory factory = Mockito.mock(NumericTileFactory.class);
    Tile createdTile = Mockito.mock(Tile.class);

    @Test
    void canBeThrowing() {
        assertThrows(ModuleException.class, () -> new NumberDivision(factory).execute(container, new NumericVariable(0)));
        Mockito.verifyNoInteractions(container, factory);
        assertThrows(ModuleException.class, () -> new NumberDivision(factory).execute(container, new NumericVariable(1), new NumericVariable(0)));
        Mockito.verifyNoInteractions(container, factory);
    }

    @Test
    void correctResult() {
        assertEquals(new NumericVariable(0.5d), new NumberDivision(factory).execute(container, new NumericVariable(2)));
        assertEquals(new NumericVariable(0.5d), new NumberDivision(factory).execute(container, new NumericVariable(1), new NumericVariable(2)));
    }

    @Test
    void factoryCommunication() {

        NumericVariable w1 = new NumberDivision(factory).execute(container, new NumericVariable(1));

        InOrder factOrd = Mockito.inOrder(factory);
        factOrd.verify(factory).getNumericTile(w1, "Division of");
        Mockito.verifyNoMoreInteractions(factory);
    }

    @Test
    void containerCommunication() {
        Mockito.when(factory.getNumericTile(new NumericVariable(1.0), "Division of")).then((x) -> createdTile);

        new NumberDivision(factory).execute(container, new NumericVariable(1));

        Mockito.verify(container).addTile(createdTile);
        Mockito.verifyNoMoreInteractions(container);
    }

    @Test
    public void verifyTest() {
        NumericVariable m = new NumericVariable(1d);
        Variable<?>[] arr0 = new Variable[]{};
        Variable<?>[] arr1 = new Variable[]{m};
        Variable<?>[] arr2 = new Variable[]{m, m};
        Variable<?>[] arr3 = new Variable[]{m, m, m};
        Variable<?>[] arr1Odd = new Variable[]{(Variable<Double>) () -> 1d};
        Variable<?>[] arr2Odd = new Variable[]{m, (Variable<Double>) () -> 1d};

        assertFalse(new NumberDivision(factory).verify(arr0));
        assertTrue(new NumberDivision(factory).verify(arr1));
        assertTrue(new NumberDivision(factory).verify(arr2));
        assertFalse(new NumberDivision(factory).verify(arr3));
        assertFalse(new NumberDivision(factory).verify(arr1Odd));
        assertFalse(new NumberDivision(factory).verify(arr2Odd));
    }

}
