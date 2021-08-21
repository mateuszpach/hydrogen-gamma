package hydrogengamma.model;

import hydrogengamma.model.modules.*;

public enum Modules {
    DETERMINANT("determinant", new Determinant()),
    DERIVATIVE("derivative", new Differentiator()),
    LEVENSHTEIN_DIST("levenshtein", new LevenshteinDistance()),
    LINEAR_SYSTEM("solve", new LinearSystemSolver()),
    MATRIX_ADD("+", new MatrixAddition()),
    MATRIX_MUL("*", new MatrixMultiplication()),
    MATRIX_SCALE("*", new MatrixScalar()),
    NUMBER_ADD("+", new NumberAddition()),
    //NUMBER_ADD2("a", new NumberAddition()),
    NUMBER_MUL("*", new NumberMultiplication()),
    NUMBER_DIV("/", new NumberDivision()),
    NUMBER_SUB("-", new NumberSubtraction()),
    NUMBER_IDENTITY("", new NumericIdentity()),
    MATRIX_IDENTITY("", new MatrixIdentity()),
    TEXT_IDENTITY("", new TextIdentity()),
    FUNCTION_IDENTITY("", new FunctionIdentity());
    // TODO: add stuff here L&M&M
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
