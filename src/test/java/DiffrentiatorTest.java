import model.modules.Diffrentiator;
import org.junit.jupiter.api.Test;
import model.variables.FunctionVariable;

import static org.junit.jupiter.api.Assertions.*;


public class DiffrentiatorTest {

    @Test
    public void trivialDerivatives() {
        FunctionVariable f1 = new FunctionVariable("sin(x)");
        FunctionVariable f2 = new FunctionVariable("cos(x)");
        FunctionVariable f3 = new FunctionVariable("e^(x)");
        FunctionVariable f4 = new FunctionVariable("ln(x)");
        FunctionVariable f5 = new FunctionVariable("x");

        Diffrentiator diff = new Diffrentiator();

        assertEquals("cos(x)", diff.execute(new FunctionVariable[]{f1}).value);
        assertEquals("(-sin(x))", diff.execute(new FunctionVariable[]{f2}).value);
        assertEquals("e^(x)", diff.execute(new FunctionVariable[]{f3}).value);
        assertEquals("1/x", diff.execute(new FunctionVariable[]{f4}).value);
        assertEquals("1", diff.execute(new FunctionVariable[]{f5}).value);
    }

    @Test
    public void wrongFormulas() {
        FunctionVariable f1 = new FunctionVariable("");
        FunctionVariable f2 = new FunctionVariable("(())");
        FunctionVariable f3 = new FunctionVariable("e^(x");
        FunctionVariable f4 = new FunctionVariable("sin+cox(x)");
        FunctionVariable f5 = new FunctionVariable("--x");

        Diffrentiator diff = new Diffrentiator();

        assertThrows(Diffrentiator.InvalidFormulaException.class, () -> diff.execute(new FunctionVariable[]{f1}));
        assertThrows(Diffrentiator.InvalidFormulaException.class, () -> diff.execute(new FunctionVariable[]{f2}));
        assertThrows(Diffrentiator.DerivativeNotKnownException.class, () -> diff.execute(new FunctionVariable[]{f3}));
        assertThrows(Diffrentiator.DerivativeNotKnownException.class, () -> diff.execute(new FunctionVariable[]{f4}));
        assertThrows(Diffrentiator.DerivativeNotKnownException.class, () -> diff.execute(new FunctionVariable[]{f5}));
    }

    @Test
    public void simpleAdditionRule() {
        FunctionVariable f1 = new FunctionVariable("sin(x)+cos(x)");
        FunctionVariable f2 = new FunctionVariable("x+e^(x)");
        FunctionVariable f3 = new FunctionVariable("e^(x)-sin(x)-x+ln(x)");
        FunctionVariable f4 = new FunctionVariable("ln(x)+sin(x)");
        FunctionVariable f5 = new FunctionVariable("x+x");

        Diffrentiator diff = new Diffrentiator();

        assertEquals("(cos(x)+(-sin(x)))", diff.execute(new FunctionVariable[]{f1}).value);
        assertEquals("(1+e^(x))", diff.execute(new FunctionVariable[]{f2}).value);
        assertEquals("(e^(x)-cos(x)-1+1/x)", diff.execute(new FunctionVariable[]{f3}).value);
        assertEquals("(1/x+cos(x))", diff.execute(new FunctionVariable[]{f4}).value);
        assertEquals("(1+1)", diff.execute(new FunctionVariable[]{f5}).value);
    }

    @Test
    public void simpleMultDiv() {
        FunctionVariable f1 = new FunctionVariable("sin(x)*cos(x)");
        FunctionVariable f2 = new FunctionVariable("x*e^(x)");
        FunctionVariable f3 = new FunctionVariable("sin(x)/cos(x)");
        FunctionVariable f4 = new FunctionVariable("sin(x)*x*cos(x)");
        FunctionVariable f5 = new FunctionVariable("e^(x)/sin(x)");

        Diffrentiator diff = new Diffrentiator();

        assertEquals("(sin(x)*(-sin(x))+cos(x)*cos(x))", diff.execute(new FunctionVariable[]{f1}).value);
        assertEquals("(x*e^(x)+e^(x)*1)", diff.execute(new FunctionVariable[]{f2}).value);
        assertEquals("(sin(x)*(-1)*(-sin(x))/(cos(x))^2+cos(x)*cos(x)/(cos(x))^2)", diff.execute(new FunctionVariable[]{f3}).value);
        assertEquals("((sin(x)*1+x*cos(x))*(-sin(x))+cos(x)*((sin(x)*0+1*cos(x))+(x*(-sin(x))+cos(x)*1)))", diff.execute(new FunctionVariable[]{f4}).value);
        assertEquals("(e^(x)*(-1)*cos(x)/(sin(x))^2+sin(x)*e^(x)/(sin(x))^2)", diff.execute(new FunctionVariable[]{f5}).value);
    }

    @Test
    public void removingParentheses() {
        FunctionVariable f1 = new FunctionVariable("(((sin(x)+cos(x))))");
        FunctionVariable f2 = new FunctionVariable("(((e^(x))))+(x)");
        FunctionVariable f3 = new FunctionVariable("((sin(x)))/((cos(x)))");
        FunctionVariable f4 = new FunctionVariable("(((sin(x)))/((cos(x)))+(x))");
        FunctionVariable f5 = new FunctionVariable("((((((e^(x))))+(x))))");

        Diffrentiator diff = new Diffrentiator();

        assertEquals("(cos(x)+(-sin(x)))", diff.execute(new FunctionVariable[]{f1}).value);
        assertEquals("(e^(x)+1)", diff.execute(new FunctionVariable[]{f2}).value);
        assertEquals("(sin(x)*(-1)*(-sin(x))/(cos(x))^2+cos(x)*cos(x)/(cos(x))^2)", diff.execute(new FunctionVariable[]{f3}).value);
        assertEquals("((sin(x)*(-1)*(-sin(x))/(cos(x))^2+cos(x)*cos(x)/(cos(x))^2)+1)", diff.execute(new FunctionVariable[]{f4}).value);
        assertEquals("(e^(x)+1)", diff.execute(new FunctionVariable[]{f5}).value);
    }

    @Test
    public void polynomials() {
        FunctionVariable f1 = new FunctionVariable("x^(51)");
        FunctionVariable f2 = new FunctionVariable("x^(-14)");
        FunctionVariable f3 = new FunctionVariable("-x^(-10)");
        FunctionVariable f4 = new FunctionVariable("x^(0)");
        FunctionVariable f5 = new FunctionVariable("sin(x)*(-5*x^(2))");

        Diffrentiator diff = new Diffrentiator();

        assertEquals("51.0*x^(50.0)", diff.execute(new FunctionVariable[]{f1}).value);
        assertEquals("-14.0*x^(-15.0)", diff.execute(new FunctionVariable[]{f2}).value);
        assertEquals("(-(-10.0*x^(-11.0)))", diff.execute(new FunctionVariable[]{f3}).value);
        assertEquals("0.0*x^(-1.0)", diff.execute(new FunctionVariable[]{f4}).value);
        assertEquals("(sin(x)*(-((5*2.0*x^(1.0)+x^(2)*0)))+-5*x^(2)*cos(x))", diff.execute(new FunctionVariable[]{f5}).value);
    }

    @Test
    public void constants() {
        FunctionVariable f1 = new FunctionVariable("100");
        FunctionVariable f2 = new FunctionVariable("-99.9");
        FunctionVariable f3 = new FunctionVariable("3*sin(x)");
        FunctionVariable f4 = new FunctionVariable("-4*cos(x)");
        FunctionVariable f5 = new FunctionVariable("x/10");

        Diffrentiator diff = new Diffrentiator();

        assertEquals("0", diff.execute(new FunctionVariable[]{f1}).value);
        assertEquals("0", diff.execute(new FunctionVariable[]{f2}).value);
        assertEquals("(3*cos(x)+sin(x)*0)", diff.execute(new FunctionVariable[]{f3}).value);
        assertEquals("(-((4*(-sin(x))+cos(x)*0)))", diff.execute(new FunctionVariable[]{f4}).value);
        assertEquals("(x*(-1)*0/(10)^2+10*1/(10)^2)", diff.execute(new FunctionVariable[]{f5}).value);
    }
}
