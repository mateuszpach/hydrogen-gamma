package hydrogengamma.model.variables;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class FunctionVariableTest {

    @Test
    void getValue() {
        FunctionVariable f = new FunctionVariable("(sin(x))");

        assertEquals("(sin(x))", f.getValue());
    }

    @Test
    void sameEqual() {
        FunctionVariable f1 = new FunctionVariable("(sin(x))");
        FunctionVariable f2 = new FunctionVariable("(sin(x))");

        assertEquals(f1, f2);
    }

    @Test
    void differentNotEqual() {
        FunctionVariable f1 = new FunctionVariable("(sin(x))");
        FunctionVariable f2 = new FunctionVariable("(cos(x))");

        assertNotEquals(f1, f2);
    }

    @Test
    void testHashCode() {
        String s = "(sin(x))";
        FunctionVariable f = new FunctionVariable(s);

        assertEquals(Objects.hash(s), f.hashCode());
    }
}