package hydrogengamma;

import hydrogengamma.controllers.Parser;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.parsers.standard.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StandardParserTest {
    @Test
    void parserWorks() {
        Loader loader = Mockito.mock(Loader.class);
        TreeBuilder treeBuilder = Mockito.mock(TreeBuilder.class);
        Computer computer = Mockito.mock(Computer.class);
        Parser parser = new StandardParser(loader, treeBuilder, computer);
        final String variables = "a=1";
        final String operations = "a+a";
        assertDoesNotThrow(() -> parser.parse(variables, operations));
    }


    @Test
    void parserCatchesParsingExceptions() {
        Loader loader = Mockito.mock(Loader.class);
        TreeBuilder treeBuilder = Mockito.mock(TreeBuilder.class);
        final TilesContainer[] container = new TilesContainer[1];
        Computer computer = (variables, expressions) -> {
            throw new ParsingException("error");
        };
        Parser parser = new StandardParser(loader, treeBuilder, computer);
        final String variables = "a=1";
        final String operations = "a+a";
        assertDoesNotThrow(() -> container[0] = parser.parse(variables, operations));
        assertEquals("$\\text{error}$", container[0].getTiles().get(0).getContent());
        assertEquals("Resolving error", container[0].getTiles().get(0).getLabel());
    }

}
