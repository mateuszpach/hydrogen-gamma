package hydrogengamma.model;

import hydrogengamma.vartiles.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserFactoryTest {
    @Test
    void getParser() {
        String variables = "x=1;y=2";
        String expression = "x+y";
        //TODO: Mateusz
        //TilesContainer result = ParserFactory.getParser().parse(variables, expression);
        // waiting for extractors to make variable tiles, get() is out of index here
        //assertEquals("x+y", result.getTiles().get(2).getLabel());
        //assertEquals("$\\text{3.0}$", result.getTiles().get(2).getContent());
    }
}