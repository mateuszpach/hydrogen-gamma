package vartiles;

import model.variables.MatrixVariable;

public class MatrixTileMaker implements TileMaker {

    private MatrixVariable matrix;

    public MatrixTileMaker(MatrixVariable mat) {
        matrix = mat;
    }

    @Override
    public String makeHtml() {
        return null;
    }
}
