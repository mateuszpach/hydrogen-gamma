package vartiles;

import model.variables.MatrixVariable;

public class MatrixTile extends DefaultTile {

    private MatrixVariable matrix;

    public MatrixTile(MatrixVariable mat) {
        matrix = mat;
    }

    @Override
    public String getContent() {
        return '$' + matrix.toString() + '$'; // TODO: change to proper format
    }
}
