package hydrogengamma.model.extractors;

import hydrogengamma.model.parsers.standard.loaders.VariableExtractor;
import hydrogengamma.model.variables.TextVariable;

public class TextVariableExtractor implements VariableExtractor<TextVariable> {
    @Override
    public TextVariable extract(String formula) {
        return new TextVariable(formula.substring(1, formula.length() - 1));
    }

    @Override
    public boolean verify(String formula) {
        return formula.length() >= 2 && formula.charAt(0) == '\"' && formula.charAt(formula.length() - 1) == '\"';
    }
}
