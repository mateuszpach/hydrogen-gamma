import java.util.ArrayList;
import java.util.Arrays;

public class MatrixVariable implements Variable {

    private final double[][] value;

    MatrixVariable(double[][] value) throws IllegalArgumentException {
        if (value == null || value.length == 0 || value[0].length == 0)
            throw new IllegalArgumentException("Values empty");
        for (double[] doubles : value)
            if (doubles.length != value[0].length)
                throw new IllegalArgumentException("Different number of columns in different rows.");

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rowsNum(); i++){
            for (int j = 0; j < colsNum(); j++) {
                builder.append(value[i][j]).append(' ');
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
