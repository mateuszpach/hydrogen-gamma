package hydrogengamma.model.parsers.standard;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;

import java.util.List;
import java.util.Map;

public interface Computer {
    TilesContainer compute(Map<String, Variable<?>> variables, List<Expression> expressions);
}
