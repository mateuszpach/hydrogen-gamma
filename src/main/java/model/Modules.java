package model;

import model.modules.*;
import model.variables.NumericVariable;

import java.util.function.BiFunction;
import java.util.function.Function;

public enum Modules {
    DETERMINANT("determinant", new Determinant()),
    DERIVATIVE("derivative", new Differentiator()),
    LEVENSHTEIN_DIST("levenshtein", new LevenshteinDistance()),
    LINEAR_SYSTEM("solve", new LinearSystemSolver()),
    MATRIX_ADD("+", new MatrixAddition()),
    MATRIX_MUL("*", new MatrixMultiplication()),
    MATRIX_SCALE("*", new MatrixScalar()),
    NUMBER_ADD("+", new NumberAddition()),
    NUMBER_MUL("*", new NumberMultiplication()),
    NUMBER_DIV("/", new NumberDivision()),
    NUMBER_SUB("-", new NumberSubtraction());
    // TODO: add stuff here
    // so constant values in expression enforce some new rules about module calling names: 1. don't make "--", it will be resolved to ""; 2. don't use numeric values or I'll replace them
    public final String name;
    public final Module<?> module;

    Modules(String name, Module<?> module) {
        this.name = name;
        this.module = module;
    }
}
