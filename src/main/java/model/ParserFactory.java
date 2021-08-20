package model;

import controllers.Parser;
import model.parsers.standard.Computer;
import model.parsers.standard.Loader;
import model.parsers.standard.StandardParser;

public class ParserFactory {
    public static Parser getParser() {
        Loader loader = new Loader();
        Computer computer = new Computer();
        return new StandardParser(loader, computer);
    }
}
