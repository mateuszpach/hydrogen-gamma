public class varBox {
    enum varType {
        MATRIX, TEXT, NUMBER, FUNCTION
    }

    private MatrixVariable matrix;
    private String text;
    private Double number;
    private FunctionVariable function;
    private final varType type;

    varBox(MatrixVariable matrix) {
        this.type = varType.MATRIX;
        this.matrix = matrix;
    }

    varBox(String text) {
        this.type = varType.TEXT;
        this.text = text;
    }

    varBox(Double number) {
        this.type = varType.NUMBER;
        this.number = number;
    }

    varBox(FunctionVariable function) {
        this.type = varType.FUNCTION;
        this.function = function;
    }

    public MatrixVariable getMatrix() {
        if (this.type != varType.MATRIX)
            throw new IllegalArgumentException("Is not a matrix");
        return this.matrix;
    }

    public Double getNumber() {
        if (this.type != varType.NUMBER)
            throw new IllegalArgumentException("Is not a number");
        return this.number;
    }

    public String getText() {
        if (this.type != varType.TEXT)
            throw new IllegalArgumentException("Is not a text");
        return this.text;
    }

    public FunctionVariable getFunction() {
        if (this.type != varType.FUNCTION)
            throw new IllegalArgumentException("Is not a function");
        return this.function;
    }

    public varType getType() {
        return this.type;
    }
}
