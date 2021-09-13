package hydrogengamma.model.parsers.standard;

import java.util.List;

public interface TreeBuilder {
    List<Expression> build(String operation);
}
