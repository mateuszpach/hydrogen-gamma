package model;

import controllers.Parser;
import model.parsers.standard.Computer;
import model.parsers.standard.Loader;
import model.parsers.standard.StandardParser;

public class ParserFactory {
    public static Parser getParser(ParserType type) {
        switch (type) {
            case STANDARD:
                Loader loader = new Loader();
                Computer computer = new Computer();
                return new StandardParser(loader, computer);
            default:
                throw new IllegalArgumentException("No such implementation found");
        }
    }

    public enum ParserType {
        STANDARD
    }
}
