package vartiles;

import model.variables.MatrixVariable;

public class MatrixTileMaker extends DefaultTileMaker {

    private MatrixVariable matrix;

    public MatrixTileMaker(MatrixVariable mat) {
        matrix = mat;
    }

    @Override
    public String getContent() {
        return '$' + matrix.toString() + '$'; // TODO: change to proper format
    }
}
