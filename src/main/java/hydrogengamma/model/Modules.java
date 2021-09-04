package hydrogengamma.model;

import hydrogengamma.model.modules.*;
import hydrogengamma.model.modules.tilefactories.GeneralTilesFactory;

public enum Modules {
    // TODO: loading test must be added, just seen all tests pass while constructor was throwing
    // Numeric
    NUMBER_IDENTITY("", new NumberIdentity()),
    NUMBER_ADD("+", new NumberAddition(new GeneralTilesFactory())),
    NUMBER_SUB("-", new NumberSubtraction(new GeneralTilesFactory())),
    NUMBER_MUL("*", new NumberMultiplication(new GeneralTilesFactory())),
    NUMBER_DIV("/", new NumberDivision(new GeneralTilesFactory())),
    NUMBER_SQRT("sqrt", new NumberSquareRoot(new GeneralTilesFactory())),
//    NUMBER_ADD2("a", new NumberAddition()),

    // Matrix
    MATRIX_IDENTITY("", new MatrixIdentity()),
    DETERMINANT("determinant", new Determinant(new GeneralTilesFactory())),
    LINEAR_SYSTEM("solve", new LinearSystemSolver(new GeneralTilesFactory())),
    MATRIX_ADD("+", new MatrixAddition(new GeneralTilesFactory())),
    MATRIX_SUB("+", new MatrixSubtraction(new GeneralTilesFactory())),
    MATRIX_MUL("*", new MatrixMultiplication(new GeneralTilesFactory())),
    MATRIX_SCALE("*", new MatrixScalar(new GeneralTilesFactory())),
    MATRIX_TRANS("transpose", new MatrixTranspose(new GeneralTilesFactory())),

    // Text
    TEXT_IDENTITY("", new TextIdentity()),
    LEVENSHTEIN_DIST("levenshtein", new LevenshteinDistance(new GeneralTilesFactory())),

    // Function
    FUNCTION_IDENTITY("", new FunctionIdentity()),
    DERIVATIVE("derivative", new Differentiator(new GeneralTilesFactory())),

    // Voids
    COUNT_LETTERS("countLetters", new CountLetters(new GeneralTilesFactory())),
    COUNT_WORDS("countWords", new CountWords(new GeneralTilesFactory())),
    LU_DECOMPOSITION("LU", new LUDecomposer(new GeneralTilesFactory())),
    LONGEST_COMMON_SUBSTRING("LCS", new LongestCommonSubstrings(new GeneralTilesFactory())),
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
