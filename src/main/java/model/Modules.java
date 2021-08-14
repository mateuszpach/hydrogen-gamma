package model;

import model.modules.*;
import model.terminalmodules.LUDecomposer;

public enum Modules {
    DETERMINANT("determinant", new Determinant()),
    DERIVATIVE("derivative", new Differentiator()),
    LEVENSHTEIN_DIST("levenshtein", new LevenshteinDistance()),
    LINEAR_SYSTEM("solve", new LinearSystemSolver()),
    MATRIX_ADD("+", new MatrixAddition()),
    MATRIX_MUL("*", new MatrixMultiplication()),
    MATRIX_SCALE("*", new MatrixScalar()),
    NUMBER_ADD("+", new NumberAddition()),
    NUMBER_MUL("*", new NumberMultiplication());

    String name;
    Module<?> module;

    Modules(String name, Module<?> module) {
        this.name = name;
        this.module = module;
    }
}
