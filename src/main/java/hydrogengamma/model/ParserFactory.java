package hydrogengamma.model;

import hydrogengamma.controllers.Parser;
import hydrogengamma.model.parsers.standard.StandardComputer;
import hydrogengamma.model.parsers.standard.StandardLoader;
import hydrogengamma.model.parsers.standard.StandardParser;

public class ParserFactory {
    public static Parser getParser() {
        StandardLoader standardLoader = new StandardLoader();
        StandardComputer standardComputer = new StandardComputer();
        return new StandardParser(standardLoader, standardComputer);
    }
}
