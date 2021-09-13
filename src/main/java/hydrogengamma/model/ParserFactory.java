package hydrogengamma.model;

import hydrogengamma.controllers.Parser;
import hydrogengamma.model.parsers.standard.*;
import hydrogengamma.model.parsers.standard.computers.StandardComputer;

public class ParserFactory {
    public static Parser getParser() {
        Loader loader = new StandardLoader();
        Computer computer = new StandardComputer();
        TreeBuilder treeBuilder = new StandardTreeBuilder();
        return new StandardParser(loader, treeBuilder, computer);
    }
}
