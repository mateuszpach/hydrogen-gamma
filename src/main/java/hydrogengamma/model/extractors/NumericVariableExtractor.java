package hydrogengamma.model.extractors;

import hydrogengamma.model.parsers.standard.loaders.VariableExtractor;
import hydrogengamma.model.variables.NumericVariable;

public class NumericVariableExtractor implements VariableExtractor<NumericVariable> {
    @Override
    public NumericVariable extract(String formula) {
        return new NumericVariable(Double.parseDouble(formula));
    }

    @Override
    public boolean verify(String formula) {
        try {
            Double.parseDouble(formula);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
