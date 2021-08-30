package hydrogengamma.model.extractors;

import hydrogengamma.model.Variable;

public interface VariableExtractor<T extends Variable<?>> {

    public T extract(String formula);
    public boolean verify(String formula);
}
