package hydrogengamma.model.variables;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TextVariableTest {

    @Test
    void getValue() {
        assertEquals("xa", new TextVariable("xa").getValue());
    }

    @Test
    void sameEqual() {
        TextVariable f1 = new TextVariable("aq");
        TextVariable f2 = new TextVariable("aq");

        assertEquals(f1, f2);
    }

    @Test
    void differentNotEqual() {
        TextVariable f1 = new TextVariable("aq");
        TextVariable f2 = new TextVariable("a2q");

        assertNotEquals(f1, f2);
    }

    @Test
    void testHashCode() {
        assertEquals(Objects.hash("qw"), new TextVariable("qw").hashCode());
    }
}