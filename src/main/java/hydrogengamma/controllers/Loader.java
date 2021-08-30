package hydrogengamma.controllers;

import hydrogengamma.model.parsers.standard.State;

public interface Loader {
    State load(String variables, String operation);
}
