package hydrogengamma.model;

import hydrogengamma.model.modules.*;
import hydrogengamma.model.modules.tilefactories.AllTilesFactory;
import hydrogengamma.model.modules.utils.ModuleException;
import hydrogengamma.model.variables.NumericVariable;

import static java.lang.Math.sin;

public enum Modules {
    // TODO: loading test must be added, just seen all tests pass while constructor was throwing
    // Numeric
    NUMBER_IDENTITY("", new NumberIdentity()),
    NUMBER_ADD_BINARY("+", new NumberBiOperator(new AllTilesFactory(), "Sum of", (a, b) -> new NumericVariable(a.getValue() + b.getValue()))),
    NUMBER_SUB_BINARY("-", new NumberBiOperator(new AllTilesFactory(), "Difference of", (a, b) -> new NumericVariable(a.getValue() - b.getValue()))),
    NUMBER_MUL_BINARY("*", new NumberBiOperator(new AllTilesFactory(), "Product of", (a, b) -> new NumericVariable(a.getValue() * b.getValue()))),
    NUMBER_DIV_BINARY("/", new NumberBiOperator(new AllTilesFactory(), "Division of", (a, b) -> {
        if (b.getValue() == 0d)
            throw new ModuleException("Division by zero: " + a.getValue() + " / " + b.getValue());
        return new NumericVariable(a.getValue() / b.getValue());
    })),
    NUMBER_SIN("sin", new NumberUnaryOperator(new AllTilesFactory(), "Sinus of", (a) -> new NumericVariable(sin(a.getValue())))),
    NUMBER_ADD_UNARY("+", new NumberUnaryOperator(new AllTilesFactory(), "Sum of", (a) -> new NumericVariable(a.getValue()))),
    NUMBER_MUL_UNARY("*", new NumberUnaryOperator(new AllTilesFactory(), "Product of", (a) -> new NumericVariable(a.getValue()))),
    NUMBER_SUB_UNARY("-", new NumberUnaryOperator(new AllTilesFactory(), "Negation of", (a) -> new NumericVariable(-a.getValue()))),
    NUMBER_DIV_UNARY("/", new NumberUnaryOperator(new AllTilesFactory(), "Inverse of", (a) -> {
        if (a.getValue() == 0d)
            throw new ModuleException("Division by zero: " + 1 + " / " + a.getValue());
        return new NumericVariable(1d / a.getValue());
    })),

    // Matrix
    MATRIX_IDENTITY("", new MatrixIdentity()),
    DETERMINANT("determinant", new Determinant(new AllTilesFactory())),
    LINEAR_SYSTEM("solve", new LinearSystemSolver(new AllTilesFactory())),
    MATRIX_ADD("+", new MatrixAddition(new AllTilesFactory())),
    MATRIX_SUB("-", new MatrixSubtraction(new AllTilesFactory())),
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
