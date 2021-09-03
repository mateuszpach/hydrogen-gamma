package hydrogengamma.model;

import hydrogengamma.model.modules.*;

public enum Modules {
    // TODO: loading test must be added, just seen all tests pass while constructor was throwing
    // Numeric
    NUMBER_IDENTITY("", new NumberIdentity()),
    NUMBER_ADD("+", new NumberAddition()),
    NUMBER_SUB("-", new NumberSubtraction()),
    NUMBER_MUL("*", new NumberMultiplication()),
    NUMBER_DIV("/", new NumberDivision()),
    NUMBER_SQRT("sqrt", new NumberSquareRoot()),
//    NUMBER_ADD2("a", new NumberAddition()),

    // Matrix
    MATRIX_IDENTITY("", new MatrixIdentity()),
    DETERMINANT("determinant", new Determinant()),
    LINEAR_SYSTEM("solve", new LinearSystemSolver()),
    MATRIX_ADD("+", new MatrixAddition()),
    MATRIX_SUB("+", new MatrixSubtraction()),
    MATRIX_MUL("*", new MatrixMultiplication()),
    MATRIX_SCALE("*", new MatrixScalar()),
    MATRIX_TRANS("transpose", new MatrixTranspose()),

    // Text
    TEXT_IDENTITY("", new TextIdentity()),
    LEVENSHTEIN_DIST("levenshtein", new LevenshteinDistance()),

    // Function
    FUNCTION_IDENTITY("", new FunctionIdentity()),
    DERIVATIVE("derivative", new Differentiator()),

    // Voids
    COUNT_LETTERS("countLetters", new CountLetters()),
    COUNT_WORDS("countWords", new CountWords()),
    LU_DECOMPOSITION("LU", new LUDecomposer()),
    LONGEST_COMMON_SUBSTRING("LCS", new LongestCommonSubstrings()),
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
