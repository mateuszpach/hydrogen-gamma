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

        System.out.println(Diffrentiation.symbolicDerivative(f1).value);
        System.out.println(Diffrentiation.symbolicDerivative(f2).value);
        System.out.println(Diffrentiation.symbolicDerivative(f3).value);

        assertEquals("sin(x)*(-sin(x))+cos(x)*cos(x))", Diffrentiation.symbolicDerivative(f1).value);
        assertEquals("x*e^(x)+e^(x)*1)", Diffrentiation.symbolicDerivative(f2).value);
        assertEquals("sin(x)*(-1)*(-sin(x))/(cos(x))^2+cos(x)*cos(x)/(cos(x))^2)", Diffrentiation.symbolicDerivative(f3).value);
    }
}
