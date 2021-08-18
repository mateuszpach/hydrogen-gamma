package model.parsers.standard;

import model.variables.FunctionVariable;
import model.variables.MatrixVariable;
import model.variables.NumericVariable;
import model.variables.TextVariable;
import utils.Pair;

import java.util.ArrayList;

import static java.lang.Math.min;

public class Loader {

    //TODO: divide into smaller classes/functions (load, compute -> "inner" class)
    //TODO: terminal module, print stuff (identity function)
    //: infix, don't start  variable with digit, parse numeric constants
    //TODO: replace #n in label with nested formula
    //TODO: try not to remove whitespace from within text variables (or don't 'cause controlling these is vital)

    public String load(String varDefinition, String operation, State state) {
        //no # allowed, also remove whitespace
        varDefinition = varDefinition.replaceAll("\\s+", "").replaceAll("#", "");
        operation = operation.replaceAll("\\s+", "").replaceAll("#", "") + ")";
        System.out.println("var:" + varDefinition + ": operation:" + operation + ":");
        try {
            this.parseVariables(varDefinition, state);
            operation = this.replaceConstants(operation, state);
            this.simplifyOperation(operation, state);
        } catch (ParsingException e) {
            return e.msg;
        }
        return null;
    }

    private String replaceConstants(String operation, State state) {// seems like highly explosive one
        String output = "";
        char[] ins = operation.toCharArray();
        for (int i = 0; i < ins.length; ++i) {//resolve double -
            while (i < ins.length - 2 && ins[i] == '-' && ins[i + 1] == '-') {
                i += 2;
            }
            output += ins[i];
        }
        operation = output;
        System.out.println("after removing --: " + operation);
        ins = operation.toCharArray();
        output = "";
        for (int i = 0; i < ins.length; ++i) {
            if ((ins[i] <= '9' && ins[i] >= '0') || ins[i] == '-') {
                int j = i;
                boolean legit = false;
                while (j < ins.length && operation.substring(i, j).matches("-?\\d*\\.?\\d*")) {
                    if (ins[j] <= '9' && ins[j] >= '0') legit = true;
                    ++j;
                }
                j--;
                if (legit) {
                    String constant = operation.substring(i, j);
                    System.out.println("Suspected constant:" + constant + ": " + i + " " + j);
                    try {
                        double x = Double.parseDouble(constant);
                        if (!state.varBoxes.containsKey("##" + x))
                            state.varBoxes.put("##" + x, new NumericVariable(x));
                        output += "##" + x;
                    } catch (Exception e) {
                        throw new ParsingException("could not process: " + constant + " as numeric constant\n");
                    }
                    i = j;
                } else
                    j = i;
                output += operation.substring(i, j);
            }
            output += ins[i];
        }
        System.out.println("after replacing constants:" + output + ":");
        return output;
    }

    private void parseVariables(String varDefinition, State state) {
        if (varDefinition.length() == 0)
            return;
        String[] vars = varDefinition.split(";");
        for (String a : vars) {
            String[] b = a.split("=");
            if (b.length != 2)
                throw new ParsingException("variable definition must contain exactly one '=' character: " + a);
            if (!b[0].matches("[a-zA-Z][\\w]*")) {
                throw new ParsingException("variable name must be alphanumeric not beginning with digit, but is:" + b[0]);
            }
            //type casing
            if (b[1].charAt(0) == '\"') {//text
                if (b[1].charAt(b[1].length() - 1) == '\"' && b[1].length() >= 2) {
                    state.varBoxes.put(b[0], new TextVariable(b[1].substring(1, b[1].length() - 1)));
                } else
                    throw new ParsingException("Text variable definition must be within '\"\"' quotation: " + a);
            } else if (b[1].charAt(0) == '(') {//function
                if (b[1].charAt(b[1].length() - 1) == ')') {
                    state.varBoxes.put(b[0], new FunctionVariable(b[1]));
                } else
                    throw new ParsingException("Function variable definition must be within () parenthesis: " + a);
            } else if (b[1].charAt(0) == '[') {//matrix
                if (b[1].charAt(b[1].length() - 1) == ']') {
                    b[1] = b[1].replaceAll("/", " / ");// looks stupid but makes rows not glue together
                    String[] rows = b[1].substring(1, b[1].length() - 1).split("/");
                    ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
                    for (String row : rows) {
                        ArrayList<Double> rowVal = new ArrayList<>();
                        String[] val = row.split(",");
                        for (String x : val) {
                            x = x.replaceAll("[^\\d-.]", "");
                            if (x.length() == 0)
                                continue;
                            try {
                                Double y = Double.parseDouble(x);
                                rowVal.add(y);
                            } catch (NumberFormatException e) {
                                throw new ParsingException(x + " from matrix definition " + a + " does not represent valid number");
                            }
                        }
                        if (rowVal.size() < 1)
                            throw new ParsingException("A row from matrix definition " + a + " was found empty");
                        matrix.add(rowVal);
                    }
                    int rowSize = matrix.get(0).size();
                    for (ArrayList<Double> row : matrix) {
                        if (row.size() != rowSize)
                            throw new ParsingException("Rows in matrix definition " + a + "must be of equal length");
                    }
                    double[][] dMatrix = new double[matrix.size()][rowSize];
                    for (int i = 0; i < matrix.size(); ++i)
                        for (int j = 0; j < rowSize; ++j)
                            dMatrix[i][j] = matrix.get(i).get(j);
                    state.varBoxes.put(b[0], new MatrixVariable(dMatrix));
                } else
                    throw new ParsingException("Matrix variable definition must be within [] parenthesis: " + a);
            } else {//numeric
                try {
                    double x = Double.parseDouble(b[1]);
                    state.varBoxes.put(b[0], new NumericVariable(x));
                } catch (NumberFormatException e) {
                    throw new ParsingException(b[1] + " from definition " + a + " does not represent valid number");
                }
            }
        }
    }

    private void simplifyOperation(String operation, State state) {
        ArrayList<String> list = new ArrayList<>();
        simplify(operation, list, state);
    }

    private String simplify(String query, ArrayList<String> list, State state) {
        char[] chars = query.toCharArray();
        int i = 0;
        while (i < query.length()) {
            if (chars[i] == '(') {//a bit like ,
                String operation = query.substring(0, i);
                ArrayList<String> myVars = new ArrayList<>();
                query = simplify(query.substring(min(i + 1, query.length())), myVars, state);
                String varName = state.getSubstitutionName();
                state.futureVariables.put(varName, new Pair<>(operation, myVars));
                list.add(varName);
                return simplify(query, list, state);
            } else if (chars[i] == ')') {//go up, and substitute
                String var = query.substring(0, i);
                if (var.length() > 0)
                    list.add(var);
                if (i + 1 == query.length())
                    query = "";
                else {
                    query = query.substring(i + 1);
                }
                return query;
            } else if (chars[i] == ',' || i + 1 == chars.length) {//add and continue
                String var = query.substring(0, i);
                if (var.length() > 0)
                    list.add(var);
                if (i + 1 == query.length())
                    query = "";
                else {
                    query = query.substring(i + 1);
                }
                return simplify(query, list, state);
            }
            ++i;
        }
        return "";
    }
}
