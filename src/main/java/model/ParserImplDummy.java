package model;

import controllers.Parser;
import model.variables.FunctionVariable;
import model.variables.MatrixVariable;
import model.variables.NumericVariable;
import model.variables.TextVariable;
import vartiles.*;

public class ParserImplDummy implements Parser {
    @Override
    public TileMakersContainer parse(String variables, String expression) {
        TileMakersContainer container = new TileMakersContainerImpl();
        DefaultTileMaker t1 = new NumericTileMaker(new NumericVariable(21.37));
        t1.setLabel("A+B");
        DefaultTileMaker t2 = new TextTileMaker(new TextVariable("Lorem ipsum."));
        t2.setLabel("C*B");
        double a[][] = {{1, 0, 1}, {0, 1, 1}, {1, 0, 1}};
        DefaultTileMaker t3 = new MatrixTileMaker(new MatrixVariable(a));
        t3.setLabel("A+B^n");
        DefaultTileMaker t4 = new FunctionTileMaker(new FunctionVariable("\\sin{x}"));
        t4.setLabel("A+B");

        container.addTileMaker(t1);
        container.addTileMaker(t2);
        container.addTileMaker(t3);
        container.addTileMaker(t4);

        return container;
    }
}
