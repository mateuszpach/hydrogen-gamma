package hydrogengamma.model.extractors;

import hydrogengamma.model.variables.FunctionVariable;

public class FunctionVariableExtractor implements VariableExtractor<FunctionVariable> {
    @Override
    public FunctionVariable extract(String formula) {
        return new FunctionVariable(formula);
    }

    @Override
    public boolean verify(String formula) {
        formula = formula.replaceAll("\\s", "");// not a text so remove all whitespace left
        return formula.length() >= 2 && formula.charAt(0) == '(' && formula.charAt(formula.length() - 1) == ')';
    }
}
