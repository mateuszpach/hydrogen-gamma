package model;

public class VarBox {

    public enum VarType {
        MATRIX, TEXT, NUMBER, FUNCTION
    }

    Variable<?> value;

    private final VarType type;
    String label;

    VarBox(Variable<?> val, VarType type, String Label) {
        this.value = val;
        this.type = type;
        this.label = Label;
    }

    public Variable<?> getValue() {
        return this.value;
    }

    public VarType getType() {
        return this.type;
    }
}
