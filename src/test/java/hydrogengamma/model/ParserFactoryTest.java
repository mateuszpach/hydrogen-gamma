package hydrogengamma.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserFactoryTest {
    @Test
    void getParser() {
        String variables = "x=1;y=2";
        String expression = "x+y";

        TilesContainer result = ParserFactory.getParser().parse(variables, expression);
        assertEquals("Sum of: $\\mathit{x, y}$", result.getTiles().get(0).getLabel());
        assertEquals("$3.0$", result.getTiles().get(0).getContent());
    }
}