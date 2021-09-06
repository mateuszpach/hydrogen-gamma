package hydrogengamma.model.extractors;

import hydrogengamma.model.Variable;

public interface VariableExtractor<T extends Variable<?>> {

    T extract(String formula);

    boolean verify(String formula);
}
