package hydrogengamma.model;

import hydrogengamma.model.extractors.*;

public enum Extractors {
    FUNCTION_VARIABLE_EXTRACTOR(new FunctionVariableExtractor()),
    MATRIX_VARIABLE_EXTRACTOR(new MatrixVariableExtractor()),
    NUMERIC_VARIABLE_EXTRACTOR(new NumericVariableExtractor()),
    TEXT_VARIABLE_EXTRACTOR(new TextVariableExtractor());

    public final VariableExtractor<?> extractor;

    Extractors(VariableExtractor<?> extractor) {
        this.extractor = extractor;
    }
}
