package hydrogengamma;

import hydrogengamma.controllers.Computer;
import hydrogengamma.controllers.Loader;
import hydrogengamma.controllers.Parser;
import hydrogengamma.controllers.TreeBuilder;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.modules.utils.ModuleException;
import hydrogengamma.model.parsers.standard.ParsingException;
import hydrogengamma.model.parsers.standard.StandardParser;
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
    void parserCatchesModuleExceptions() {
        Loader loader = Mockito.mock(Loader.class);
        TreeBuilder treeBuilder = Mockito.mock(TreeBuilder.class);
        final TilesContainer[] container = new TilesContainer[1];
        Computer computer = (variables, expressions) -> {
            throw new ModuleException() {
                @Override
                public String toString() {
                    return "error";
                }
            };
        };
        Parser parser = new StandardParser(loader, treeBuilder, computer);
        final String variables = "a=1";
        final String operations = "a+a";
        assertDoesNotThrow(() -> container[0] = parser.parse(variables, operations));
        assertEquals("$\\text{error}$", container[0].getTiles().get(0).getContent());
        assertEquals("Module error", container[0].getTiles().get(0).getLabel());
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
