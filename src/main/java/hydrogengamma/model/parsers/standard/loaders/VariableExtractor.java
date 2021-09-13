package hydrogengamma.model.parsers.standard.loaders;

import hydrogengamma.model.parsers.standard.Variable;

public interface VariableExtractor<T extends Variable<?>> {

    T extract(String formula);

    boolean verify(String formula);
}
