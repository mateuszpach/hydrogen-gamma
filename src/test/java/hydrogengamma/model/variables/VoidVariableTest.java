package hydrogengamma.model.variables;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class VoidVariableTest {
    @Test
    void getValue() {
        assertNull(new VoidVariable().getValue());
    }
}