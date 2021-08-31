package hydrogengamma.model;

import hydrogengamma.controllers.Computer;
import hydrogengamma.controllers.Loader;
import hydrogengamma.controllers.Parser;
import hydrogengamma.controllers.TreeBuilder;
import hydrogengamma.model.parsers.standard.StandardComputer;
import hydrogengamma.model.parsers.standard.StandardLoader;
import hydrogengamma.model.parsers.standard.StandardParser;
import hydrogengamma.model.parsers.standard.StandardTreeBuilder;

public class ParserFactory {
    public static Parser getParser() {
        Loader loader = new StandardLoader();
        Computer computer = new StandardComputer();
        TreeBuilder treeBuilder = new StandardTreeBuilder();
        return new StandardParser(loader, treeBuilder, computer);
    }
}
