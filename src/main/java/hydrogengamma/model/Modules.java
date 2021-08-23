package hydrogengamma.model;

import hydrogengamma.model.modules.*;

public enum Modules {
    // Numeric
    NUMBER_IDENTITY("", new NumericIdentity()),
    DERIVATIVE("derivative", new Differentiator()),
    NUMBER_ADD("+", new NumberAddition()),
    NUMBER_SUB("-", new NumberSubtraction()),
    NUMBER_MUL("*", new NumberMultiplication()),
    NUMBER_DIV("/", new NumberDivision()),
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
    FUNCTION_IDENTITY("", new FunctionIdentity());

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
