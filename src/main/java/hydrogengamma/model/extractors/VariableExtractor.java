package hydrogengamma.model.extractors;

import hydrogengamma.model.Variable;

public interface VariableExtractor<T extends Variable<?>> {
    //TODO: cool we got those, would you mind using them LUKASZ

    T extract(String formula);

    boolean verify(String formula);
}
