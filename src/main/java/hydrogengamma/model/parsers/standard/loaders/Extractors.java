package hydrogengamma.model.parsers.standard.loaders;

import hydrogengamma.model.extractors.FunctionVariableExtractor;
import hydrogengamma.model.extractors.MatrixVariableExtractor;
import hydrogengamma.model.extractors.NumericVariableExtractor;
import hydrogengamma.model.extractors.TextVariableExtractor;

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
