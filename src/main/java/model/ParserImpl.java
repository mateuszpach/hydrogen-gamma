package model;

import controllers.Parser;
import model.variables.NumericVariable;
import model.variables.TextVariable;
import utils.Pair;
import model.variables.MatrixVariable;
import model.variables.FunctionVariable;
import vartiles.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.min;

public class ParserImpl implements Parser {
    public Map<String, VarBox> varBoxes;
    private TileMakersContainer container;

    public Map<String, String> modules;

    public ParserImpl() {
        modules = new HashMap<>();
        //some module loader would be appreciated
    }

    @Override
    public TileMakersContainer parse(String variables, String expression) {
        this.container = new TileMakersContainerImpl();
        if (variables.equals("") && expression.equals(""))
            return this.container;
        String msg;
        if ((msg = this.load(variables, expression)) != null) {
            this.container = new TileMakersContainerImpl();
            this.container.addTileMaker(new comunicate(msg).setLabel("Parsing error"));
        }
        return this.container;
    }

    public Map<String, Pair<String, ArrayList<String>>> futureVariables;
    int futureIndex;

    public String load(String varDefinition, String operation) {
        //no # allowed, also remove whitespace
        varDefinition = varDefinition.replaceAll("\\s+", "").replaceAll("#", "");
        operation = operation.replaceAll("\\s+", "").replaceAll("#", "");
        varBoxes = new HashMap<>();
        futureVariables = new HashMap<>();
        this.futureIndex = 0;
        try {
            this.parseVariables(varDefinition);
            this.simplifyOperation(operation);
        } catch (ParsingException e) {
            return e.msg;
        }
        for (String key : varBoxes.keySet()) {
            System.out.println("variable:" + key + " " + varBoxes.get(key).getVar().getValue());
            this.container.addTileMaker(new comunicate(varBoxes.get(key).getVar().getValue().toString()).setLabel(key));
        }
        for (String key : futureVariables.keySet()) {
            System.out.println("operation:" + key + " " + futureVariables.get(key).first);
            System.out.print(" -----> ");
            for (String a : futureVariables.get(key).second)
                System.out.print(a + " ");
            System.out.println();
            String tmp = futureVariables.get(key).first + " ( ";
            for (String i : futureVariables.get(key).second)
                tmp = tmp + i + ',';
            tmp = tmp.substring(0, tmp.length() - 1) + ')';
            this.container.addTileMaker(new comunicate(key + " : " + tmp).setLabel(key));
        }
        return null;
    }

    public void compute() {
        //here goes some kind of switch that computes #0,#1,#2, ... and returns the last one
        //why not yet implemented? model.modules are still written in somewhat random fashion
        //from #0 to <futureIndex
    }

    private void parseVariables(String varDefinition) {
        String[] vars = varDefinition.split(";");
        for (String a : vars) {
            String[] b = a.split(":");
            if (b.length != 2)
                throw new ParsingException("model.Variable definition must contain exactly one ':' character: " + a);
            if (b[1].charAt(0) == '\"') {//text
                if (b[1].charAt(b[1].length() - 1) == '\"' && b[1].length() >= 2) {
                    varBoxes.put(b[0], new VarBox(new TextVariable(b[1])));
                } else
                    throw new ParsingException("Text variable definition must contain exactly two '\"' character at front and end: " + a);
            } else if (b[1].charAt(0) == '(') {//function
                if (b[1].charAt(b[1].length() - 1) == ')') {
                    //TODO: consult whether parsing functionVariable requires further parsing or if it just accepts any string
                    varBoxes.put(b[0], new VarBox(new FunctionVariable(b[1])));
                } else
                    throw new ParsingException("Function variable definition must be within () characters: " + a);
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
                    varBoxes.put(b[0], new VarBox(new MatrixVariable(dMatrix)));
                } else
                    throw new ParsingException("Matrix variable definition must be within [] characters: " + a);
            } else {//numeric
                try {
                    double x = Double.parseDouble(b[1]);
                    varBoxes.put(b[0], new VarBox(new NumericVariable(x)));
                } catch (NumberFormatException e) {
                    throw new ParsingException(b[1] + " from definition " + a + " does not represent valid number");
                }
            }
        }
    }

    private void simplifyOperation(String operation) {
        ArrayList<String> list = new ArrayList<>();
        simplify(operation, list);
    }

    public static class ParsingException extends IllegalArgumentException {
        String msg = "";

        public ParsingException(String s) {
            super(s);
            msg = s;
        }
    }

    private class comunicate extends DefaultTileMaker {
        String msg = "";

        comunicate(String msg) {
            super();
            this.msg = msg;
        }

        @Override
        public String getLabel() {
            return this.label;
        }

        @Override
        public String getContent() {
            return msg;
        }
    }

    String getSubstitutionName() {
        return "#" + futureIndex++;
    }

    private String simplify(String query, ArrayList<String> list) {
        char[] chars = query.toCharArray();
        int i = 0;
        while (i < query.length()) {
            if (chars[i] == '(') {//a bit like ,
                String operation = query.substring(0, i);
                ArrayList<String> myVars = new ArrayList<>();
                query = simplify(query.substring(min(i + 1, query.length())), myVars);
                String varName = getSubstitutionName();
                futureVariables.put(varName, new Pair<>(operation, myVars));
                list.add(varName);
                return simplify(query, list);
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
                return simplify(query, list);
            }
            ++i;
        }
        return "";
    }


}
