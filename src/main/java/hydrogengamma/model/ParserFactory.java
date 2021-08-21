package hydrogengamma.model;

import hydrogengamma.controllers.Parser;
import hydrogengamma.model.parsers.standard.Computer;
import hydrogengamma.model.parsers.standard.Loader;
import hydrogengamma.model.parsers.standard.StandardParser;

public class ParserFactory {
    public static Parser getParser() {
        Loader loader = new Loader();
        Computer computer = new Computer();
        return new StandardParser(loader, computer);
    }
}
