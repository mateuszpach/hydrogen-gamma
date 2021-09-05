package hydrogengamma.model;

import hydrogengamma.model.modules.*;
import hydrogengamma.model.modules.tilefactories.AllTilesFactory;
import hydrogengamma.model.modules.utils.ModuleException;
import hydrogengamma.model.variables.NumericVariable;

public enum Modules {
    // TODO: loading test must be added, just seen all tests pass while constructor was throwing
    // Numeric
    NUMBER_IDENTITY("", new NumberIdentity()),
    NUMBER_ADD("+", new NumberBiOperator(new AllTilesFactory(), "Sum of", (a, b) -> new NumericVariable(a.getValue() + b.getValue()))),
    NUMBER_SUB("-", new NumberBiOperator(new AllTilesFactory(), "Difference of", (a, b) -> new NumericVariable(a.getValue() - b.getValue()))),
    NUMBER_MUL("*", new NumberBiOperator(new AllTilesFactory(), "Product of", (a, b) -> new NumericVariable(a.getValue() * b.getValue()))),
    NUMBER_DIV("/", new NumberBiOperator(new AllTilesFactory(), "Division of", (a, b) -> {
        if (b.getValue() == 0d)
            throw new ModuleException("Division by zero: " + a + " / " + b);
        return new NumericVariable(a.getValue() / b.getValue());
    })),
    NUMBER_ADD2("add", new NumberBiOperator(new AllTilesFactory(), "Sum of", (a, b) -> new NumericVariable(a.getValue() + b.getValue()))),

    // Matrix
    MATRIX_IDENTITY("", new MatrixIdentity()),
    DETERMINANT("determinant", new Determinant(new AllTilesFactory())),
    LINEAR_SYSTEM("solve", new LinearSystemSolver(new AllTilesFactory())),
    MATRIX_ADD("+", new MatrixAddition(new AllTilesFactory())),
    MATRIX_SUB("+", new MatrixSubtraction(new AllTilesFactory())),
    MATRIX_MUL("*", new MatrixMultiplication(new AllTilesFactory())),
    MATRIX_SCALE("*", new MatrixScalar(new AllTilesFactory())),
    MATRIX_TRANS("transpose", new MatrixTranspose(new AllTilesFactory())),

    // Text
    TEXT_IDENTITY("", new TextIdentity()),
    LEVENSHTEIN_DIST("levenshtein", new LevenshteinDistance(new AllTilesFactory())),

    // Function
    FUNCTION_IDENTITY("", new FunctionIdentity()),
    DERIVATIVE("derivative", new Differentiator(new AllTilesFactory())),

    // Voids
    COUNT_LETTERS("countLetters", new CountLetters(new AllTilesFactory())),
    COUNT_WORDS("countWords", new CountWords(new AllTilesFactory())),
    LU_DECOMPOSITION("LU", new LUDecomposer(new AllTilesFactory())),
    LONGEST_COMMON_SUBSTRING("LCS", new LongestCommonSubstrings(new AllTilesFactory())),
    VOID_IDENTITY("", new VoidIdentity());

    // TODO: add stuff here L&M
    public final String name;
    public final Module<?> module;

    Modules(String name, Module<?> module) {
        if (!name.matches("^([a-zA-Z]*|[+-:/^!&*|])$")) {
            throw new IllegalArgumentException("Module name must be either alphabetic or one of +-:/^!&*| or empty.");
        }
        this.name = name;
        this.module = module;
    }
}
