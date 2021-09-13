package hydrogengamma.controllers.vartiles;

import hydrogengamma.model.variables.MatrixVariable;

public class MatrixTile extends DefaultTile {

    private final MatrixVariable matrix;

    public MatrixTile(MatrixVariable mat, String label) {
        super(label);
        matrix = mat;
    }

    @Override
    public String getContent() {
        StringBuilder res = new StringBuilder();
        res.append("\\begin{bmatrix}");
        for (int i = 0; i < matrix.rowsNum(); i++) {
            for (int j = 0; j < matrix.colsNum(); j++) {
                res.append(matrix.get(i, j));
                if (j < matrix.colsNum() - 1) {
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
