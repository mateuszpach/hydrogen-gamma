package hydrogengamma.model;

import hydrogengamma.controllers.Parser;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.vartiles.*;

public class ParserImplDummy implements Parser {
    @Override
    public TilesContainer parse(String variables, String expression) {
        TilesContainer container = new TilesContainerImpl();
        DefaultTile t1 = new NumericTile(new NumericVariable(21.37));
        t1.setLabel("A+B");
        DefaultTile t2 = new TextTile(new TextVariable("Lorem ipsum."));
        t2.setLabel("C*B");
        double[][] a = {{1, 0, 1}, {0, 1, 1}, {1, 0, 1}};
        DefaultTile t3 = new MatrixTile(new MatrixVariable(a));
        t3.setLabel("A+B^n: \\text{substitution #1}");
        DefaultTile t4 = new FunctionTile(new FunctionVariable("(e^{x}-sin(x))-x+\\ln{(x)}"));
        t4.setLabel("A+B");

        container.addTile(t1);
        container.addTile(t2);
        container.addTile(t3);
        container.addTile(t4);

        return container;
    }
}
