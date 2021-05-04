import java.util.ArrayList;

public class MatrixVariable implements Variable {

    private final Double[][] value;

    MatrixVariable(Double[][] value) throws IllegalArgumentException {
        if (value == null || value.length == 0 || value[0].length == 0)
            throw new IllegalArgumentException();
        this.value = value;
    }

    public Double get(int i, int j) {
        return value[i][j];
    }

    public Integer rowsNum() {
        return value.length;
    }

    public Integer colsNum() {
        return value[0].length;
    }
}
