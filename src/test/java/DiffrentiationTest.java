import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class DiffrentiationTest {

    @Test
    public void trivialDerivatives() {
        FunctionVariable f1 = new FunctionVariable("sin(x)");
        FunctionVariable f2 = new FunctionVariable("cos(x)");
        FunctionVariable f3 = new FunctionVariable("e^(x)");
        FunctionVariable f4 = new FunctionVariable("ln(x)");
        FunctionVariable f5 = new FunctionVariable("x");

        assertEquals("cos(x)", Diffrentiation.symbolicDerivative(f1).value);
        assertEquals("(-sin(x))", Diffrentiation.symbolicDerivative(f2).value);
        assertEquals("e^(x)", Diffrentiation.symbolicDerivative(f3).value);
        assertEquals("1/x", Diffrentiation.symbolicDerivative(f4).value);
        assertEquals("1", Diffrentiation.symbolicDerivative(f5).value);
    }

    @Test
    public void wrongFormulas() {
        FunctionVariable f1 = new FunctionVariable("");
        FunctionVariable f2 = new FunctionVariable("(())");
        FunctionVariable f3 = new FunctionVariable("e^(x");
        FunctionVariable f4 = new FunctionVariable("sin+cox(x)");
        FunctionVariable f5 = new FunctionVariable("--x");

        assertThrows(Diffrentiation.InvalidFormulaException.class, () -> Diffrentiation.symbolicDerivative(f1));
        assertThrows(Diffrentiation.InvalidFormulaException.class, () -> Diffrentiation.symbolicDerivative(f2));
        assertThrows(Diffrentiation.DerivativeNotKnownException.class, () -> Diffrentiation.symbolicDerivative(f3));
        assertThrows(Diffrentiation.DerivativeNotKnownException.class, () -> Diffrentiation.symbolicDerivative(f4));
        assertThrows(Diffrentiation.DerivativeNotKnownException.class, () -> Diffrentiation.symbolicDerivative(f5));
    }

    @Test
    public void simpleAdditionRule() {
        FunctionVariable f1 = new FunctionVariable("sin(x)+cos(x)");
        FunctionVariable f2 = new FunctionVariable("x+e^(x)");
        FunctionVariable f3 = new FunctionVariable("e^(x)-sin(x)-x+ln(x)");
        FunctionVariable f4 = new FunctionVariable("ln(x)+sin(x)");
        FunctionVariable f5 = new FunctionVariable("x+x");

        assertEquals("(cos(x)+(-sin(x)))", Diffrentiation.symbolicDerivative(f1).value);
        assertEquals("(1+e^(x))", Diffrentiation.symbolicDerivative(f2).value);
        assertEquals("(e^(x)-cos(x)-1+1/x)", Diffrentiation.symbolicDerivative(f3).value);
        assertEquals("(1/x+cos(x))", Diffrentiation.symbolicDerivative(f4).value);
        assertEquals("(1+1)", Diffrentiation.symbolicDerivative(f5).value);
    }

    @Test
    public void simpleMultDiv() {
        FunctionVariable f1 = new FunctionVariable("sin(x)*cos(x)");
        FunctionVariable f2 = new FunctionVariable("x*e^(x)");
        FunctionVariable f3 = new FunctionVariable("sin(x)/cos(x)");
        FunctionVariable f4 = new FunctionVariable("sin(x)*x*cos(x)");
        FunctionVariable f5 = new FunctionVariable("e^(x)/sin(x)");


        assertEquals("(sin(x)*(-sin(x))+cos(x)*cos(x))", Diffrentiation.symbolicDerivative(f1).value);
        assertEquals("(x*e^(x)+e^(x)*1)", Diffrentiation.symbolicDerivative(f2).value);
        assertEquals("(sin(x)*(-1)*(-sin(x))/(cos(x))^2+cos(x)*cos(x)/(cos(x))^2)", Diffrentiation.symbolicDerivative(f3).value);
        assertEquals("((sin(x)*1+x*cos(x))*(-sin(x))+cos(x)*((sin(x)*0+1*cos(x))+(x*(-sin(x))+cos(x)*1)))", Diffrentiation.symbolicDerivative(f4).value);
        assertEquals("(e^(x)*(-1)*cos(x)/(sin(x))^2+sin(x)*e^(x)/(sin(x))^2)", Diffrentiation.symbolicDerivative(f5).value);
    }

    @Test
    public void removingParentheses() {
        FunctionVariable f1 = new FunctionVariable("(((sin(x)+cos(x))))");
        FunctionVariable f2 = new FunctionVariable("(((e^(x))))+(x)");
        FunctionVariable f3 = new FunctionVariable("((sin(x)))/((cos(x)))");
        FunctionVariable f4 = new FunctionVariable("(((sin(x)))/((cos(x)))+(x))");
        FunctionVariable f5 = new FunctionVariable("((((((e^(x))))+(x))))");

        assertEquals("(cos(x)+(-sin(x)))", Diffrentiation.symbolicDerivative(f1).value);
        assertEquals("(e^(x)+1)", Diffrentiation.symbolicDerivative(f2).value);
        assertEquals("(sin(x)*(-1)*(-sin(x))/(cos(x))^2+cos(x)*cos(x)/(cos(x))^2)", Diffrentiation.symbolicDerivative(f3).value);
        assertEquals("((sin(x)*(-1)*(-sin(x))/(cos(x))^2+cos(x)*cos(x)/(cos(x))^2)+1)", Diffrentiation.symbolicDerivative(f4).value);
        assertEquals("(e^(x)+1)", Diffrentiation.symbolicDerivative(f5).value);
    }

    @Test
    public void polynomials() {
        FunctionVariable f1 = new FunctionVariable("x^(51)");
        FunctionVariable f2 = new FunctionVariable("x^(-14)");
        FunctionVariable f3 = new FunctionVariable("-x^(-10)");
        FunctionVariable f4 = new FunctionVariable("x^(0)");
        FunctionVariable f5 = new FunctionVariable("sin(x)*(-5*x^(2))");

        assertEquals("51.0*x^(50.0)", Diffrentiation.symbolicDerivative(f1).value);
        assertEquals("-14.0*x^(-15.0)", Diffrentiation.symbolicDerivative(f2).value);
        assertEquals("(-(-10.0*x^(-11.0)))", Diffrentiation.symbolicDerivative(f3).value);
        assertEquals("0.0*x^(-1.0)", Diffrentiation.symbolicDerivative(f4).value);
        assertEquals("(sin(x)*(-((5*2.0*x^(1.0)+x^(2)*0)))+-5*x^(2)*cos(x))", Diffrentiation.symbolicDerivative(f5).value);
    }

    @Test
    public void constants() {
        FunctionVariable f1 = new FunctionVariable("100");
        FunctionVariable f2 = new FunctionVariable("-99.9");
        FunctionVariable f3 = new FunctionVariable("3*sin(x)");
        FunctionVariable f4 = new FunctionVariable("-4*cos(x)");
        FunctionVariable f5 = new FunctionVariable("x/10");

        assertEquals("0", Diffrentiation.symbolicDerivative(f1).value);
        assertEquals("0", Diffrentiation.symbolicDerivative(f2).value);
        assertEquals("(3*cos(x)+sin(x)*0)", Diffrentiation.symbolicDerivative(f3).value);
        assertEquals("(-((4*(-sin(x))+cos(x)*0)))", Diffrentiation.symbolicDerivative(f4).value);
        assertEquals("(x*(-1)*0/(10)^2+10*1/(10)^2)", Diffrentiation.symbolicDerivative(f5).value);
    }
}
