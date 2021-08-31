package hydrogengamma.controllers;

import java.util.List;

public interface TreeBuilder {
    List<Expression> build(String operation);
}
