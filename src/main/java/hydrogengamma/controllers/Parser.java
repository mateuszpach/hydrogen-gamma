package hydrogengamma.controllers;

import hydrogengamma.model.parsers.standard.TilesContainer;

public interface Parser {
    TilesContainer parse(String variables, String expression);
}
