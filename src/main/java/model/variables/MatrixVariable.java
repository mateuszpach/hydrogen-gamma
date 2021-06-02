package model.variables;
import model.Variable;

public class MatrixVariable extends Variable<double[][]> {

    public MatrixVariable(double[][] value) throws IllegalArgumentException {
        if (value == null || value.length == 0 || value[0].length == 0)
            throw new IllegalArgumentException("Values empty");
        for (double[] doubles : value)
            if (doubles.length != value[0].length)
                throw new IllegalArgumentException("Different number of columns in different rows.");

        this.value = value.clone();
    }

    @Override
    public double[][] getValue() {
        return value.clone();
    }

    public double get(int i, int j) {
        return value[i][j];
    }

    public int rowsNum() {
        return value.length;
    }

    public int colsNum() {
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

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != MatrixVariable.class)
            return false;
        MatrixVariable m = (MatrixVariable) o;
        if (rowsNum() != m.rowsNum() || colsNum() != m.colsNum())
            return false;
        for (int i = 0; i < rowsNum(); i++) {
            for (int j = 0; j < colsNum(); j++) {
                if (get(i, j) != m.get(i, j))
                    return false;
            }
        }
        return true;
    }
}
