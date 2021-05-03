public class Variable {
    enum Type {
        NUMBER,
        FUNCTION,
        MATRIX,
        TEXT
    }

    Type type;

    double number;
    Func function;
    double[][] matrix;
    String text;
}
