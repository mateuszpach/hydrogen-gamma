package hydrogengamma.model.parsers.standard.computers;

import hydrogengamma.controllers.AllTilesFactory;
import hydrogengamma.model.modules.*;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.NumericVariable;

import static java.lang.Math.sin;

public enum Modules {
    // Numeric
    NUMBER_IDENTITY("", new NumberIdentity()),
    NUMBER_ADD_BINARY("+", new NumberBiOperator(new AllTilesFactory(), "Sum of", (a, b) -> new NumericVariable(a.getValue() + b.getValue()))),
    NUMBER_SUB_BINARY("-", new NumberBiOperator(new AllTilesFactory(), "Difference of", (a, b) -> new NumericVariable(a.getValue() - b.getValue()))),
    NUMBER_MUL_BINARY("*", new NumberBiOperator(new AllTilesFactory(), "Product of", (a, b) -> new NumericVariable(a.getValue() * b.getValue()))),
    NUMBER_SIN("sin", new NumberUnaryOperator(new AllTilesFactory(), "Sinus of", (a) -> new NumericVariable(sin(a.getValue())))),
    NUMBER_ADD_UNARY("+", new NumberUnaryOperator(new AllTilesFactory(), "Sum of", (a) -> new NumericVariable(a.getValue()))),
    NUMBER_MUL_UNARY("*", new NumberUnaryOperator(new AllTilesFactory(), "Product of", (a) -> new NumericVariable(a.getValue()))),
    NUMBER_SUB_UNARY("-", new NumberUnaryOperator(new AllTilesFactory(), "Negation of", (a) -> new NumericVariable(-a.getValue()))),
    NUMBER_DIV("/", new NumberDivision(new AllTilesFactory())),

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
    DERIVATIVE("derivative", new Differentiator(new AllTilesFactory())),
    FUNCTION_ADD_BINARY("+", new FunctionBiOperator(new AllTilesFactory(), "Sum of", (f, g) -> new FunctionVariable("(" + f.getValue() + "+" + g.getValue() + ")"))),
    FUNCTION_SUB_BINARY("-", new FunctionBiOperator(new AllTilesFactory(), "Sum of", (f, g) -> new FunctionVariable("(" + f.getValue() + "-" + g.getValue() + ")"))),
    FUNCTION_MUL_BINARY("*", new FunctionBiOperator(new AllTilesFactory(), "Product of", (f, g) -> new FunctionVariable("(" + f.getValue() + "*" + g.getValue() + ")"))),
    FUNCTION_DIV_BINARY("/", new FunctionBiOperator(new AllTilesFactory(), "Division of", (f, g) -> new FunctionVariable("(" + f.getValue() + "/" + g.getValue() + ")"))),
    FUNCTION_IDENTITY("", new FunctionIdentity()),

    // Voids
    COUNT_LETTERS("countLetters", new CountLetters(new AllTilesFactory())),
    COUNT_WORDS("countWords", new CountWords(new AllTilesFactory())),
    LU_DECOMPOSITION("LU", new LUDecomposer(new AllTilesFactory())),
    LONGEST_COMMON_SUBSTRING("LCS", new LongestCommonSubstrings(new AllTilesFactory())),
    VOID_IDENTITY("", new VoidIdentity());

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
