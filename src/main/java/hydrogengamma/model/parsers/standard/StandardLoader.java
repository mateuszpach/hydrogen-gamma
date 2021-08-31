package hydrogengamma.model.parsers.standard;

import hydrogengamma.controllers.Loader;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.variables.TextVariable;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class StandardLoader implements Loader {

    public Map<String, Variable<?>> load(String varDefinition) {
        Map<String, Variable<?>> variables = new TreeMap<>();
        //no # allowed, also remove whitespace
        varDefinition = varDefinition.replaceAll("#", "");
        System.out.println("var:" + varDefinition + ":");
        this.parseVariables(varDefinition, variables);
        // so tree flattening is dumb and would be easily resolved by making identity modules not print anything
        return variables;
    }

    /* TODO:shall be removed

    private String replaceConstants(String operation, State state) {// seems like highly explosive one
        StringBuilder builder = new StringBuilder();
        char[] ins = operation.toCharArray();
        for (int i = 0; i < ins.length; ++i) {
            if ((ins[i] <= '9' && ins[i] >= '0') || ins[i] == '-') {
                int j = i;
                boolean legit = false;
                while (j < ins.length && operation.substring(i, j).matches("-?\\d*\\.?\\d*")) {
                    if (ins[j] <= '9' && ins[j] >= '0') { // IGNORE THIS WARNING, IT'S FAKE!!!
                        legit = true;
                        // don't break it, intellij is wrong here, I need 'j' to increment while it can without breaking it prematurely, 'legit' is a side check
                        // break here would parse "500" into 3 variables 5,0,0, while 'legit' check just makes sure a stranded '-' won't be tried to be parsed as a number

                        // break; DON'T
                    }
                    ++j;
                }
                j--;
                if (legit) {
                    String constant = operation.substring(i, j);
                    System.out.println("Suspected constant:" + constant + ": " + i + " " + j);
                    try {
                        double x = Double.parseDouble(constant);
                        if (!state.containsKey(state.constantName(x)))
                            state.addExpression(state.constantName(x), new State.Expression(Double.toString(x), new NumericVariable(x)));
                        builder.append(state.constantName(x));
                    } catch (Exception e) {
                        throw new ParsingException("could not process: " + constant + " as numeric constant\n");
                    }
                    i = j;
                } else
                    j = i;
                builder.append(operation, i, j);
            }
            builder.append(ins[i]);
        }
        System.out.println("after replacing constants:" + builder + ":");
        return builder.toString();
    }
    */

    // TODO Extractors dla parsera zwracające odpowiednie typy varaible LUKASZ
    // note: varDefinition will contain whitespace, remove it from everywhere BUT textVariable content (don't leave it in variables names)
    private void parseVariables(String varDefinition, Map<String, Variable<?>> state) {
        if (varDefinition.length() == 0)
            return;
        String[] vars = varDefinition.split(";");
        for (String a : vars) {
            String[] b = a.split("=");
            if (b.length != 2)
                throw new ParsingException("variable definition must contain exactly one '=' character: " + a);
            b[0] = b[0].replaceAll("\\s+", ""); // remove whitespace from name
            b[1] = b[1].strip();// strip leading and trailing whitespace from definition
            if (!b[0].matches("[a-zA-Z][\\w]*")) {
                throw new ParsingException("variable name must be alphanumeric not beginning with digit, but is:" + b[0]);
            }
            //type casing
            if (b[1].charAt(0) == '\"') {//text
                if (b[1].charAt(b[1].length() - 1) == '\"' && b[1].length() >= 2) {
                    state.put(b[0], new TextVariable(b[1].substring(1, b[1].length() - 1)));
                } else
                    throw new ParsingException("Text variable definition must be within '\"\"' quotation: " + a);
            } else {
                b[1] = b[1].replaceAll("\\s+", "");// not a text so remove all whitespace left
                if (b[1].charAt(0) == '(') {//function
                    if (b[1].charAt(b[1].length() - 1) == ')') {
                        state.put(b[0], new FunctionVariable(b[1]));
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
                        state.put(b[0], new MatrixVariable(dMatrix));
                    } else
                        throw new ParsingException("Matrix variable definition must be within [] parenthesis: " + a);
                } else {//numeric
                    try {
                        double x = Double.parseDouble(b[1]);
                        state.put(b[0], new NumericVariable(x));
                    } catch (NumberFormatException e) {
                        throw new ParsingException(b[1] + " from definition " + a + " does not represent valid number");
                    }
                }

            }
        }
    }

}
/*
Solution:
resolve inner mess, then try resolving mess in function name
any instance of something_alphabetic(...) treat as another module, meaning last variable name in operation key string is module name,
unless last char is sign, than treat innards as variable from identity module
Problem
a=2
+(a++-(a),a+a*a/(a+a(a))*a,+(a+a*(-a+a),(-a),++-+-3.66))
var:a=2: operation:+(a++-(a),a+a*a/(a+a(a))*a,+(a+a*(-a+a),(-a),++-+-3.66))):
pre fixing: +(a++-(a),a+a*a/(a+a(a))*a,+(a+a*(-a+a),(-a),++-+-3.66)))
post fixing: +(a-(a),a+a*a/(a+a(a))*a,+(a+a*(-a+a),(-a),+3.66)))
after removing --: +(a-(a),a+a*a/(a+a(a))*a,+(a+a*(-a+a),(-a),+3.66)))
Suspected constant:3.66: 44 48
after replacing constants:+(a-(a),a+a*a/(a+a(a))*a,+(a+a*(-a+a),(-a),+03d66))):
delinquent to resolve: a    resolved to: a
delinquent to resolve: a    resolved to: a
delinquent to resolve: 1var    resolved to: 1var
delinquent to resolve: -a+a    resolved to: + ( -a , a )
delinquent to resolve: -a    resolved to: - ( a )
delinquent to resolve: 3var    resolved to: 3var
delinquent to resolve: 4var    resolved to: 4var
delinquent to resolve: +03d66    resolved to: + ( 03d66 )
delinquent to resolve: 0var    resolved to: 0var
delinquent to resolve: 2var    resolved to: 2var
delinquent to resolve: *a    resolved to: * ( a )
delinquent to resolve: 5var    resolved to: 5var
variable:a 2.0
variable:03d66 3.66
future: 0var = a- ( a )
future: 1var = a+a ( a )
future: 2var = a+a*a/ ( 1var )
future: 3var = a+a* ( -a+a )
future: 4var =  ( -a )
future: 5var = + ( 3var 4var +03d66 )
future: 6var = + ( 0var 2var *a 5var )

 */
