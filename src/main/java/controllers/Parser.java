package controllers;

import model.TilesContainer;

public interface Parser {
    TilesContainer parse(String variables, String expression);
}
