import util.Pair;

import java.util.*;

import static java.lang.Math.min;

public class Parser {
    private String varDefinition;
    private String operation;
    Map<String, String> modules;
    Map<String, varBox> variables;
    Map<String, Pair<String, ArrayList<String>>> futureVariables;
    int futureIndex;

    Parser() {
        modules = new HashMap<>();
        modules.put("+", "+");
        modules.put("-", "-");
        modules.put("*", "*");
        modules.put("/", "/");
    }

    private enum varType {
        MATRIX, TEXT, NUMBER, FUNCTION
    }

    public void load(String varDefinition, String operation) {
        this.varDefinition = varDefinition;
        this.operation = "+(" + operation + ")";
        variables = new HashMap<>();
        futureVariables = new HashMap<>();
        this.futureIndex = 0;
        this.parseVariables();
        this.computeOperation();
        for (String key : variables.keySet()) {
            System.out.println(key + " " + variables.get(key).type);
            switch (variables.get(key).type) {
                case FUNCTION:
                    System.out.println(variables.get(key).getFunction().value);
                    break;
                case NUMBER:
                    System.out.println(variables.get(key).getNumber());
                    break;
                case MATRIX:
                    System.out.println(variables.get(key).getMatrix().toString());
                    break;
                case TEXT:
                    System.out.println(variables.get(key).getText());
                    break;
            }
        }
        for (String key : futureVariables.keySet()) {
            System.out.println(key + " " + futureVariables.get(key).first);
            System.out.print(" -----> ");
            for (String a : futureVariables.get(key).second)
                System.out.print(a + " ");
            System.out.println();
        }
        //here goes some kind of switch that computes #0,#1,#2, ... and returns the last one
        //why not yet implemented? modules are still written in somewhat random fashion
    }

    private void parseVariables() {
        String[] vars = varDefinition.split(";");
        for (String a : vars) {
            String[] b = a.split(":");
            if (b.length != 2)
                throw new IllegalArgumentException("Variable definition must contain exactly one ':' character: " + a);
            if (b[1].charAt(0) == '\"') {//text
                if (b[1].charAt(b[1].length() - 1) == '\"') {
                    variables.put(b[0], new varBox(b[1]));
                } else
                    throw new IllegalArgumentException("Text variable definition must contain exactly two '\"' character at front and end: " + a);
            } else if (b[1].charAt(0) == '(') {//function
                if (b[1].charAt(b[1].length() - 1) == ')') {
                    String[] c = b[1].substring(1, b[1].length() - 1).split(",");
                    double[] function = new double[c.length]; //ready for implementation of functionVariable
                    int i = 0;
                    for (String val : c) {
                        try {
                            double x = Double.parseDouble(val);
                            function[i++] = x;
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException(val + " from function definition " + a + " does not represent valid number");
                        }
                    }
                    variables.put(b[0], new varBox(new FunctionVariable(b[1])));
                } else
                    throw new IllegalArgumentException("Function variable definition must be within () characters: " + a);
            } else if (b[1].charAt(0) == '[') {//matrix
                if (b[1].charAt(b[1].length() - 1) == ']') {
                    String[] rows = b[1].substring(1, b[1].length() - 1).split("\\|");
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
                                throw new IllegalArgumentException(x + " from matrix definition " + a + " does not represent valid number");
                            }
                        }
                        matrix.add(rowVal);
                    }
                    int rowSize = matrix.get(0).size();
                    for (ArrayList<Double> row : matrix) {
                        if (row.size() != rowSize)
                            throw new IllegalArgumentException("Rows in matrix definition " + a + "must be of equal length");
                    }
                    double[][] dMatrix = new double[matrix.size()][rowSize];
                    for (int i = 0; i < matrix.size(); ++i)
                        for (int j = 0; j < rowSize; ++j)
                            dMatrix[i][j] = matrix.get(i).get(j);
                    variables.put(b[0], new varBox(new MatrixVariable(dMatrix)));
                } else
                    throw new IllegalArgumentException("Matrix variable definition must be within [] characters: " + a);
            } else {//numeric
                try {
                    double x = Double.parseDouble(b[1]);
                    variables.put(b[0], new varBox(x));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(b[1] + " from definition " + a + " does not represent valid number");
                }
            }
        }
    }

    private void computeOperation() {
        ArrayList<String> list = new ArrayList<>();
        simplify(this.operation, list);
    }

    String getSubstitutionName() {
        return "#" + futureIndex++;
    }

    private String simplify(String query, ArrayList<String> list) {
        if (query.length() == 0)
            return query;
        char[] chars = query.toCharArray();
        int i = 0;
        while (i < query.length()) {
            if (chars[i] == '(') {//a bit like ,
                String operation = query.substring(0, i);
                ArrayList<String> myVars = new ArrayList<>();
                //System.out.println("case (: "+operation + " on: "+query.substring(min(i+1,query.length())));
                query = simplify(query.substring(min(i + 1, query.length())), myVars);
                String varName = getSubstitutionName();
                futureVariables.put(varName, new Pair<>(operation, myVars));
                list.add(varName);
                return simplify(query, list);
                //return "";
            } else if (chars[i] == ')') {//go up, and substitute
                String var = query.substring(0, i);
                if (var.length() > 0)
                    list.add(var);
                if (i + 1 == query.length())
                    query = "";
                else {
                    query = query.substring(i + 1);
                }
                //System.out.println("case ,: "+ var+ " on: "+query);
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
                //System.out.println("case ,: "+ var+ " on: "+query);
                return simplify(query, list);
            }
            ++i;
        }
        return "";
    }


    private static class varBox {
        private MatrixVariable matrix;
        private String text;
        private Double number;
        private FunctionVariable function;
        private final varType type;

        varBox(MatrixVariable matrix) {
            this.type = varType.MATRIX;
            this.matrix = matrix;
        }

        varBox(String text) {
            this.type = varType.TEXT;
            this.text = text;
        }

        varBox(Double number) {
            this.type = varType.NUMBER;
            this.number = number;
        }

        varBox(FunctionVariable function) {
            this.type = varType.FUNCTION;
            this.function = function;
        }

        MatrixVariable getMatrix() {
            if (this.type != varType.MATRIX)
                throw new IllegalArgumentException("Is not a matrix");
            return this.matrix;
        }

        Double getNumber() {
            if (this.type != varType.NUMBER)
                throw new IllegalArgumentException("Is not a number");
            return this.number;
        }

        String getText() {
            if (this.type != varType.TEXT)
                throw new IllegalArgumentException("Is not a text");
            return this.text;
        }

        FunctionVariable getFunction() {
            if (this.type != varType.FUNCTION)
                throw new IllegalArgumentException("Is not a function");
            return this.function;
        }
    }
}
