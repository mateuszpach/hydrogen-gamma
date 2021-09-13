package hydrogengamma.model.parsers.standard;

import hydrogengamma.controllers.Parser;
import hydrogengamma.controllers.vartiles.InfoTile;
import hydrogengamma.model.parsers.standard.tilescontainers.TilesContainerImpl;

public class StandardParser implements Parser {

    private final Loader loader;
    private final Computer computer;
    private final TreeBuilder treeBuilder;

    public StandardParser(Loader loader, TreeBuilder treeBuilder, Computer computer) {
        this.loader = loader;
        this.computer = computer;
        this.treeBuilder = treeBuilder;
    }

    @Override
    public TilesContainer parse(String variables, String operation) {
        TilesContainer container;
        try {
            container = computer.compute(loader.load(variables), treeBuilder.build(operation));
        } catch (ParsingException exception) {
            return new TilesContainerImpl(new InfoTile(exception.toString(), "Resolving error"));
        }
        return container;

    }
}
