package hydrogengamma.controllers;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.parsers.standard.State;

public interface Computer {
    TilesContainer compute(State state);
}
