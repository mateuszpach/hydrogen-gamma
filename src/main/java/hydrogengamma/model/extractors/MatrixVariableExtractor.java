package hydrogengamma.model.extractors;

import hydrogengamma.model.variables.MatrixVariable;

import java.util.ArrayList;

public class MatrixVariableExtractor implements VariableExtractor<MatrixVariable> {

    @Override
    public MatrixVariable extract(String formula) {
        ArrayList<ArrayList<String>> rows = getRows(formula);
        double[][] grid = new double[rows.size()][rows.get(0).size()];
        for (int i = 0; i < rows.size(); i++) {
            for (int j = 0; j < rows.get(0).size(); j++) {
                grid[i][j] = Double.parseDouble(rows.get(i).get(j));
            }
        }
        return new MatrixVariable(grid);
    }

    @Override
    public boolean verify(String formula) {
        if (formula.length() < 2 || formula.charAt(0) != '[' || formula.charAt(formula.length() - 1) != ']')
            return false;
        ArrayList<ArrayList<String>> rows = getRows(formula);

        // non-empty rows check
        for (ArrayList<String> row : rows) {
            if (row.isEmpty())
                return false;
        }

        // number check
        for (ArrayList<String> row : rows) {
            for (String x : row) {
                try {
                    Double.parseDouble(x);
                } catch (NumberFormatException e) { // not a number in matrix
                    return false;
                }
            }
        }

        // rows equal size check
        int rowSize = rows.get(0).size();
        for (ArrayList<String> row : rows) {
            if (row.size() != rowSize)
                return false;
        }

        return true;
    }

    private ArrayList<ArrayList<String>> getRows(String formula) { // empty spaces are omited
        formula = formula.replaceAll("/", " / ");
        String[] rowStrings = formula.substring(1, formula.length() - 1).split("/");
        ArrayList<ArrayList<String>> rows = new ArrayList<>();

        for (String row : rowStrings) {
            ArrayList<String> rowVal = new ArrayList<>();
            String[] val = row.split(",");
            for (String x : val) {
                x = x.replaceAll("\\s", "");//remove whitespace
                if (x.length() == 0)
                    continue;
                rowVal.add(x);
            }
            rows.add(rowVal);
        }

        return rows;
    }
}
