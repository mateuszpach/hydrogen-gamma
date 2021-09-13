package hydrogengamma.controllers;

import hydrogengamma.model.TilesContainer;

public interface Parser {
    TilesContainer parse(String variables, String expression);
}
