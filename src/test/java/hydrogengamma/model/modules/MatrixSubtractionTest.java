package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.tilefactories.MatrixTileFactory;
import hydrogengamma.model.modules.utils.ModuleException;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixSubtractionTest {
    TilesContainer container = Mockito.mock(TilesContainer.class);
    MatrixTileFactory factory = Mockito.mock(MatrixTileFactory.class);
    Tile createdTile = Mockito.mock(Tile.class);

    @Test
    void canBeThrowing() {
        assertThrows(ModuleException.class, () -> new MatrixSubtraction(factory).execute(container, new MatrixVariable(new double[][]{{1, 2}}), new MatrixVariable(new double[][]{{1}, {2}})));
        Mockito.verifyNoInteractions(container, factory);
    }

    @Test
    void correctResult() {
        assertEquals(new MatrixVariable(new double[][]{{0}}), new MatrixSubtraction(factory).execute(container, new MatrixVariable(new double[][]{{1}}), new MatrixVariable(new double[][]{{1}})));
    }

    @Test
    void factoryCommunication() {

        MatrixVariable w1 = new MatrixSubtraction(factory).execute(container, new MatrixVariable(new double[][]{{1}}), new MatrixVariable(new double[][]{{1}}));

        InOrder factOrd = Mockito.inOrder(factory);
        factOrd.verify(factory).getMatrixTile(w1, "Difference of");
        Mockito.verifyNoMoreInteractions(factory);
    }

    @Test
    void containerCommunication() {
        Mockito.when(factory.getMatrixTile(new MatrixVariable(new double[][]{{0}}), "Difference of")).then((x) -> createdTile);

        new MatrixSubtraction(factory).execute(container, new MatrixVariable(new double[][]{{1}}), new MatrixVariable(new double[][]{{1}}));

        Mockito.verify(container).addTile(createdTile);
        Mockito.verifyNoMoreInteractions(container);
    }

    @Test
    public void verifyTest() {
        MatrixVariable m = new MatrixVariable(new double[][]{{1}});
        Variable<?>[] arr0 = new Variable[]{};
        Variable<?>[] arr1 = new Variable[]{m};
        Variable<?>[] arr2 = new Variable[]{m, m};
        Variable<?>[] arr3 = new Variable[]{m, m, m};
        Variable<?>[] arr1Odd = new Variable[]{m, (Variable<double[][]>) () -> new double[][]{{1d}}};
        Variable<?>[] arr2Odd = new Variable[]{(Variable<double[][]>) () -> new double[][]{{1d}}, m};

        assertFalse(new MatrixSubtraction(factory).verify(arr0));
        assertFalse(new MatrixSubtraction(factory).verify(arr1));
        assertTrue(new MatrixSubtraction(factory).verify(arr2));
        assertFalse(new MatrixSubtraction(factory).verify(arr3));
        assertFalse(new MatrixSubtraction(factory).verify(arr1Odd));
        assertFalse(new MatrixSubtraction(factory).verify(arr2Odd));
    }
}
