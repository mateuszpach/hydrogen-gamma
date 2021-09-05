package hydrogengamma.model;

import hydrogengamma.model.modules.*;
import hydrogengamma.model.modules.tilefactories.AllTilesFactory;

public enum Modules {
    // TODO: loading test must be added, just seen all tests pass while constructor was throwing
    // Numeric
    NUMBER_IDENTITY("", new NumberIdentity()),
    NUMBER_ADD("+", new NumberAddition(new AllTilesFactory())),
    NUMBER_SUB("-", new NumberSubtraction(new AllTilesFactory())),
    NUMBER_MUL("*", new NumberMultiplication(new AllTilesFactory())),
    NUMBER_DIV("/", new NumberDivision(new AllTilesFactory())),
    NUMBER_SQRT("sqrt", new NumberSquareRoot(new AllTilesFactory())),
//    NUMBER_ADD2("a", new NumberAddition()),

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
