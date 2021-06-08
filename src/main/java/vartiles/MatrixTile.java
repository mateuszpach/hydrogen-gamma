package vartiles;

import model.variables.MatrixVariable;

public class MatrixTile extends DefaultTile {

    private MatrixVariable matrix;

    public MatrixTile(MatrixVariable mat) {
        matrix = mat;
    }

    @Override
    public String getContent() {
        StringBuilder res = new StringBuilder();
        res.append("\\begin{bmatrix}");
        for (int i = 0; i < matrix.rowsNum(); i++) {
            for (int j = 0; j < matrix.rowsNum(); j++) {
                res.append(matrix.get(i, j));
                if (j < matrix.rowsNum() - 1) {
                    res.append("&");
                } else {
                    res.append("\\\\");
                }
            }
        }
        res.append("\\end{bmatrix}");
        return '$' + res.toString() + '$';
    }
}
