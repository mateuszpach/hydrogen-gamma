package controllers;

import model.TileMakersContainer;

public interface Parser {
    TileMakersContainer parse(String variables, String expression);
}
