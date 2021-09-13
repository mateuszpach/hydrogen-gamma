package hydrogengamma.controllers;

import hydrogengamma.model.parsers.standard.Computer;
import hydrogengamma.model.parsers.standard.Loader;
import hydrogengamma.model.parsers.standard.StandardParser;
import hydrogengamma.model.parsers.standard.TreeBuilder;
import hydrogengamma.model.parsers.standard.computers.StandardComputer;
import hydrogengamma.model.parsers.standard.loaders.StandardLoader;
import hydrogengamma.model.parsers.standard.treebuilders.StandardTreeBuilder;

public class ParserFactory {
    public static Parser getParser() {
        Loader loader = new StandardLoader();
        Computer computer = new StandardComputer();
        TreeBuilder treeBuilder = new StandardTreeBuilder();
        return new StandardParser(loader, treeBuilder, computer);
    }
}
