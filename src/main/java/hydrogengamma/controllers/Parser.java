package hydrogengamma.controllers;

import hydrogengamma.model.TilesContainer;

public interface Parser {
    TilesContainer parse(String variables, String expression);
}
// use 3 classes:
// loader (TBD):         string variable -> "results" variable
// treeMaker:            string operation -> operation tree (state)
// computer:             results + state -> tiles
// results: Map<id,variable>  ((given or from operation line or number of operation (best as it is now ) ) <- extractors here
// state (recipes) : ordered list of operations as function( subexpressions (list of strings (variables names) ), name of result (id), label: NOT A CLASS, JUST LIST
// tiles : as it is <- modules here


//extractor as:
//  bool verify(String)
//  Variable<?> get(String)
