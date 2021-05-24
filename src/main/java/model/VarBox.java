package model;

import model.variables.MatrixVariable;
import model.variables.FunctionVariable;

public class VarBox {

    public enum VarType {
        MATRIX, TEXT, NUMBER, FUNCTION
    }

    private MatrixVariable matrix;
    private String text;
    private Double number;
    private FunctionVariable function;
    private final VarType type;

    VarBox(MatrixVariable matrix) {
        this.type = VarType.MATRIX;
        this.matrix = matrix;
    }

    VarBox(String text) {
        this.type = VarType.TEXT;
        this.text = text;
    }

    VarBox(Double number) {
        this.type = VarType.NUMBER;
        this.number = number;
    }

    VarBox(FunctionVariable function) {
        this.type = VarType.FUNCTION;
        this.function = function;
    }

    public MatrixVariable getMatrix() {
        if (this.type != VarType.MATRIX)
            throw new IllegalArgumentException("Is not a matrix");
        return this.matrix;
    }

    public Double getNumber() {
        if (this.type != VarType.NUMBER)
            throw new IllegalArgumentException("Is not a number");
        return this.number;
    }

    public String getText() {
        if (this.type != VarType.TEXT)
            throw new IllegalArgumentException("Is not a text");
        return this.text;
    }

    public FunctionVariable getFunction() {
        if (this.type != VarType.FUNCTION)
            throw new IllegalArgumentException("Is not a function");
        return this.function;
    }

    public VarType getType() {
        return this.type;
    }
}
