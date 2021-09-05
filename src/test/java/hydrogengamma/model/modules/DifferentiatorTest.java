package hydrogengamma.model.modules;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.TilesContainerImpl;
import hydrogengamma.model.Variable;
import hydrogengamma.model.modules.tilefactories.FunctionTileFactory;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;


public class DifferentiatorTest {

    TilesContainer container =  Mockito.mock(TilesContainer.class);
    FunctionTileFactory factory = Mockito.mock(FunctionTileFactory.class);
    Tile createdTile = null;

    @Test
    public void trivialDerivatives() {
        FunctionVariable f1 = new FunctionVariable("sin(x)");
        FunctionVariable f2 = new FunctionVariable("cos(x)");
        FunctionVariable f3 = new FunctionVariable("e^(x)");
        FunctionVariable f4 = new FunctionVariable("ln(x)");
        FunctionVariable f5 = new FunctionVariable("x");

        Differentiator diff = new Differentiator(factory);

        assertEquals("cos(x)", diff.execute(container, f1).getValue());
        assertEquals("(-sin(x))", diff.execute(container, f2).getValue());
        assertEquals("e^(x)", diff.execute(container, f3).getValue());
        assertEquals("1/x", diff.execute(container, f4).getValue());
        assertEquals("1", diff.execute(container, f5).getValue());
    }

    @Test
    public void wrongFormulas() {
        FunctionVariable f1 = new FunctionVariable("");
        FunctionVariable f2 = new FunctionVariable("(())");
        FunctionVariable f3 = new FunctionVariable("e^(x");
        FunctionVariable f4 = new FunctionVariable("sin+cox(x)");
        FunctionVariable f5 = new FunctionVariable("--x");

        Differentiator diff = new Differentiator(factory);

        assertThrows(Differentiator.InvalidFormulaException.class, () -> diff.execute(container, f1));
        assertThrows(Differentiator.InvalidFormulaException.class, () -> diff.execute(container, f2));
        assertThrows(Differentiator.DerivativeNotKnownException.class, () -> diff.execute(container, f3));
        assertThrows(Differentiator.DerivativeNotKnownException.class, () -> diff.execute(container, f4));
        assertThrows(Differentiator.DerivativeNotKnownException.class, () -> diff.execute(container, f5));
    }

    @Test
    public void simpleAdditionRule() {
        FunctionVariable f1 = new FunctionVariable("sin(x)+cos(x)");
        FunctionVariable f2 = new FunctionVariable("x+e^(x)");
        FunctionVariable f3 = new FunctionVariable("e^(x)-sin(x)-x+ln(x)");
        FunctionVariable f4 = new FunctionVariable("ln(x)+sin(x)");
        FunctionVariable f5 = new FunctionVariable("x+x");

        Differentiator diff = new Differentiator(factory);

        assertEquals("(cos(x)+(-sin(x)))", diff.execute(container, f1).getValue());
        assertEquals("(1+e^(x))", diff.execute(container, f2).getValue());
        assertEquals("(e^(x)-cos(x)-1+1/x)", diff.execute(container, f3).getValue());
        assertEquals("(1/x+cos(x))", diff.execute(container, f4).getValue());
        assertEquals("(1+1)", diff.execute(container, f5).getValue());
    }

    @Test
    public void simpleMultDiv() {
        FunctionVariable f1 = new FunctionVariable("sin(x)*cos(x)");
        FunctionVariable f2 = new FunctionVariable("x*e^(x)");
        FunctionVariable f3 = new FunctionVariable("sin(x)/cos(x)");
        FunctionVariable f4 = new FunctionVariable("sin(x)*x*cos(x)");
        FunctionVariable f5 = new FunctionVariable("e^(x)/sin(x)");

        Differentiator diff = new Differentiator(factory);

        assertEquals("(sin(x)*(-sin(x))+cos(x)*cos(x))", diff.execute(container, f1).getValue());
        assertEquals("(x*e^(x)+e^(x)*1)", diff.execute(container, f2).getValue());
        assertEquals("(sin(x)*(-1)*(-sin(x))/(cos(x))^2+cos(x)*cos(x)/(cos(x))^2)", diff.execute(container, f3).getValue());
        assertEquals("((sin(x)*1+x*cos(x))*(-sin(x))+cos(x)*((sin(x)*0+1*cos(x))+(x*(-sin(x))+cos(x)*1)))", diff.execute(container, f4).getValue());
        assertEquals("(e^(x)*(-1)*cos(x)/(sin(x))^2+sin(x)*e^(x)/(sin(x))^2)", diff.execute(container, f5).getValue());
    }

    @Test
    public void removingParentheses() {
        FunctionVariable f1 = new FunctionVariable("(((sin(x)+cos(x))))");
        FunctionVariable f2 = new FunctionVariable("(((e^(x))))+(x)");
        FunctionVariable f3 = new FunctionVariable("((sin(x)))/((cos(x)))");
        FunctionVariable f4 = new FunctionVariable("(((sin(x)))/((cos(x)))+(x))");
        FunctionVariable f5 = new FunctionVariable("((((((e^(x))))+(x))))");

        Differentiator diff = new Differentiator(factory);

        assertEquals("(cos(x)+(-sin(x)))", diff.execute(container, f1).getValue());
        assertEquals("(e^(x)+1)", diff.execute(container, f2).getValue());
        assertEquals("(sin(x)*(-1)*(-sin(x))/(cos(x))^2+cos(x)*cos(x)/(cos(x))^2)", diff.execute(container, f3).getValue());
        assertEquals("((sin(x)*(-1)*(-sin(x))/(cos(x))^2+cos(x)*cos(x)/(cos(x))^2)+1)", diff.execute(container, f4).getValue());
        assertEquals("(e^(x)+1)", diff.execute(container, f5).getValue());
    }

    @Test
    public void polynomials() {
        FunctionVariable f1 = new FunctionVariable("x^(51)");
        FunctionVariable f2 = new FunctionVariable("x^(-14)");
        FunctionVariable f3 = new FunctionVariable("-x^(-10)");
        FunctionVariable f4 = new FunctionVariable("x^(0)");
        FunctionVariable f5 = new FunctionVariable("sin(x)*(-5*x^(2))");

        Differentiator diff = new Differentiator(factory);

        assertEquals("51.0*x^(50.0)", diff.execute(container, f1).getValue());
        assertEquals("-14.0*x^(-15.0)", diff.execute(container, f2).getValue());
        assertEquals("(-(-10.0*x^(-11.0)))", diff.execute(container, f3).getValue());
        assertEquals("0.0*x^(-1.0)", diff.execute(container, f4).getValue());
        assertEquals("(sin(x)*(-((5*2.0*x^(1.0)+x^(2)*0)))+-5*x^(2)*cos(x))", diff.execute(container, f5).getValue());
    }

    @Test
    public void constants() {
        FunctionVariable f1 = new FunctionVariable("100");
        FunctionVariable f2 = new FunctionVariable("-99.9");
        FunctionVariable f3 = new FunctionVariable("3*sin(x)");
        FunctionVariable f4 = new FunctionVariable("-4*cos(x)");
        FunctionVariable f5 = new FunctionVariable("x/10");

        Differentiator diff = new Differentiator(factory);

        assertEquals("0", diff.execute(container, f1).getValue());
        assertEquals("0", diff.execute(container, f2).getValue());
        assertEquals("(3*cos(x)+sin(x)*0)", diff.execute(container, f3).getValue());
        assertEquals("(-((4*(-sin(x))+cos(x)*0)))", diff.execute(container, f4).getValue());
        assertEquals("(x*(-1)*0/(10)^2+10*1/(10)^2)", diff.execute(container, f5).getValue());
    }

    @Test
    public void verifyTest() {
        FunctionVariable f = new FunctionVariable("cos(x)");
        Variable<?>[] arr1 = new Variable[]{f};
        Variable<?>[] arr2 = new Variable[]{f, f};
        Variable<?>[] arr3 = new Variable[]{new MatrixVariable(new double[][]{{0}})};

        assertTrue(new Differentiator(factory).verify(arr1));
        assertFalse(new Differentiator(factory).verify(arr2));
        assertFalse(new Differentiator(factory).verify(arr3));
    }

    @Test
    void factoryCommunication() {
        FunctionVariable f1 = new FunctionVariable("sin(x)");
        FunctionVariable f2 = new FunctionVariable("cos(x)");
        FunctionVariable e1 = new FunctionVariable("cos(x)");
        FunctionVariable e2 = new FunctionVariable("(-sin(x))");
        Differentiator diff = new Differentiator(factory);

        diff.execute(container, f1);
        diff.execute(container, f2);

        InOrder factOrd = Mockito.inOrder(factory);
        factOrd.verify(factory).getFunctionTile(e1, "Derivative of");
        factOrd.verify(factory).getFunctionTile(e2, "Derivative of");
        Mockito.verifyNoMoreInteractions(factory);
    }

    @Test
    void containerCommunication() {
        FunctionVariable f1 = new FunctionVariable("sin(x)");
        FunctionVariable e1 = new FunctionVariable("cos(x)");
        Differentiator diff = new Differentiator(factory);
        Mockito.when(factory.getFunctionTile(e1, "Derivative of")).then((x) -> {
            createdTile = Mockito.mock(Tile.class);
            return createdTile;
        });

        diff.execute(container, f1);

        Mockito.verify(container).addTile(createdTile);
        Mockito.verifyNoMoreInteractions(container);
    }

    @Test
    public void exceptionMessageTest() {
        Differentiator diff = new Differentiator(factory);
        try {
            diff.execute(new TilesContainerImpl(), new FunctionVariable("ax"));
        }
        catch (Differentiator.DerivativeNotKnownException e) {
            assertEquals("Couldn't find a derivative for function: ax", e.toString());
        }
        try {
            diff.execute(new TilesContainerImpl(), new FunctionVariable("(sin(x))**(cos(x))"));
        }
        catch (Differentiator.InvalidFormulaException e) {
            assertEquals("Function formula is invalid: (sin(x))**(cos(x))", e.toString());
        }
        try {
            diff.execute(new TilesContainerImpl(), new FunctionVariable("*(sin(x))*(cos(x))"));
        }
        catch (Differentiator.InvalidFormulaException e) {
            assertEquals("Function formula is invalid: *(sin(x))*(cos(x))", e.toString());
        }
    }
}
