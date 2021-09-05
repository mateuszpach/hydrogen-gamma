package hydrogengamma.model.variables;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class NumericVariableTest {

    @Test
    void getValue() {
        assertEquals(5.03, new NumericVariable(5.03).getValue());
    }

    @Test
    void sameEqual() {
        NumericVariable f1 = new NumericVariable(5.01);
        NumericVariable f2 = new NumericVariable(5.01);

        assertEquals(f1, f2);
    }

    @Test
    void differentNotEqual() {
        NumericVariable f1 = new NumericVariable(5.01);
        NumericVariable f2 = new NumericVariable(5.02);

        assertNotEquals(f1, f2);
    }

    @Test
    void testHashCode() {
        assertEquals(Objects.hash(5.01), new NumericVariable(5.01).hashCode());
    }
}