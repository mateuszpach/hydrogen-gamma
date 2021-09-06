package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.modules.tilefactories.NumericTileFactory;
import hydrogengamma.model.modules.utils.ModuleException;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NumberBiOperatorTest {

    TilesContainer container = Mockito.mock(TilesContainer.class);
    NumericTileFactory factory = Mockito.mock(NumericTileFactory.class);
    Tile createdTile = Mockito.mock(Tile.class);

    @Test
    void canBeThrowing() {
        NumberBiOperator throwing = new NumberBiOperator(factory, "throw", (a, b) -> {
            if (a.getValue().equals(b.getValue()))
                throw new ModuleException("throwing for no good reason");
            return new NumericVariable(a.getValue() + b.getValue());
        });

        assertThrows(ModuleException.class, () -> throwing.execute(container, new NumericVariable(1), new NumericVariable(1)));
        Mockito.verifyNoInteractions(container, factory);
    }

    @Test
    void correctResult() {
        NumberBiOperator add = new NumberBiOperator(factory, "+", (a, b) -> new NumericVariable(a.getValue() + b.getValue()));
        assertEquals(new NumericVariable(10d), add.execute(container, new NumericVariable(3), new NumericVariable(7)));
    }

    @Test
    void factoryCommunication() {
        NumberBiOperator add = new NumberBiOperator(factory, "+", (a, b) -> new NumericVariable(a.getValue() + b.getValue()));

        NumericVariable w1 = add.execute(container, new NumericVariable(1), new NumericVariable(2));

        InOrder factOrd = Mockito.inOrder(factory);
        factOrd.verify(factory).getNumericTile(w1, "+");
        Mockito.verifyNoMoreInteractions(factory);
    }
/*
    @Test
    void containerCommunication() {
        NumberBiOperator add=new NumberBiOperator(factory,"+",(a,b)->new NumericVariable(a.getValue()+b.getValue()));
        Mockito.when(factory.getNumericTile(new NumericVariable(94.0), "Determinant of")).then((x) -> createdTile);

        add.execute(container, new NumericVariable(1),new NumericVariable(2));

        Mockito.verify(container).addTile(createdTile);
        Mockito.verifyNoMoreInteractions(container);
    }

    @Test
    public void verifyTest() {
        MatrixVariable m = new MatrixVariable(new double[][]{{0, 1}, {1, 0}});
        Variable<?>[] arr1 = new Variable[]{m};
        Variable<?>[] arr2 = new Variable[]{m, m};
        Variable<?>[] arr3 = new Variable[]{new FunctionVariable("sin(x)")};

        assertTrue(new Determinant(factory).verify(arr1));
        assertFalse(new Determinant(factory).verify(arr2));
        assertFalse(new Determinant(factory).verify(arr3));
    }

 */
}
