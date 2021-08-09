package model;

import controllers.Parser;
import model.variables.FunctionVariable;
import model.variables.MatrixVariable;
import model.variables.NumericVariable;
import model.variables.TextVariable;
import utils.Pair;
import vartiles.DefaultTile;

import java.util.*;

import static java.lang.Math.min;

// it can't be run in parallel. Should it though? Never have it been mentioned in this project, even once
//
//On the topic of more steps in interface, it makes parser depend on types returned by those steps and prevents from finding different ways of parsing
//also there would be a lot thing to return/pass that's another reason to let parser use it's internal state in calculations
public class ParserImpl implements Parser {
    public Map<String, Variable<?>> varBoxes;
    private TilesContainer container;

    public ParserImpl() {
    }

    @Override
    public TilesContainer parse(String variables, String expression) {
        this.container = new TilesContainerImpl();
        if (variables.equals("") && expression.equals(""))
            return this.container;
        String msg;
        varBoxes = new TreeMap<>();
        futureVariables = new TreeMap<>();
        this.futureIndex = 0;
        if ((msg = this.load(variables, expression)) != null) {
            this.container = new TilesContainerImpl();
            this.container.addTile(new communicate(msg).setLabel("Parsing error"));
            return this.container;
        }
        Set<String> keys = varBoxes.keySet();
        String[] aKeys = keys.toArray(new String[keys.size()]);
        Collections.reverse(Arrays.asList(aKeys));
        for (String key : aKeys) {
            System.out.println("variable:" + key + " " + varBoxes.get(key).getValue());
            this.container.addTile(new communicate(varBoxes.get(key).getValue().toString()).setLabel(key));
        }//after loading print variables
        if ((msg = this.compute()) != null) {
            this.container = new TilesContainerImpl();
            this.container.addTile(new communicate(msg).setLabel("Calculating error"));
            return this.container;
        }
        return this.container;
    }

    public Map<String, Pair<String, ArrayList<String>>> futureVariables;
    int futureIndex;

    public String load(String varDefinition, String operation) {
        //no # allowed, also remove whitespace
        varDefinition = varDefinition.replaceAll("\\s+", "").replaceAll("#", "");
        operation = operation.replaceAll("\\s+", "").replaceAll("#", "");
        varBoxes = new TreeMap<>();
        futureVariables = new TreeMap<>();
        this.futureIndex = 0;
        try {
            this.parseVariables(varDefinition);
            this.simplifyOperation(operation);
        } catch (ParsingException e) {
            return e.msg;
        }
        return null;
    }

    public String compute() {
        int lastVar = futureIndex;
        futureIndex = 0;
        for (int i = 0; i < lastVar; ++i) {
            String varName = getSubstitutionName(i);
            Pair<String, ArrayList<String>> recipe = futureVariables.get(varName);

            Variable<?>[] components = new Variable[recipe.second.size()];
            for (int j = 0; j < recipe.second.size(); ++j) {
                String var = recipe.second.get(j);
                if (varBoxes.containsKey(var)) {
                    components[j] = varBoxes.get(var);
                } else {
                    return "Could not find variable " + var + "when computing #" + i + "\n";
                }
            }

            EnumSet.allOf(Modules.class)
                    .stream()
                    .filter(x -> x.name.equals(recipe.first))
                    .forEach(x -> {
                        Module<?> module = x.module;
                        if (module.verify(components)) {
                            Variable<?> got = module.execute(this.container, components);
                            varBoxes.put(varName, got);
                            String result = String.format("%s(%s)=%s\n",
                                    recipe.first,
                                    String.join(",", recipe.second),
                                    got.getValue().toString());
                            this.container.addTile(new communicate(result).setLabel(varName));
                        }
                    });

            EnumSet.allOf(TerminalModules.class)
                    .stream()
                    .filter(x -> x.name.equals(recipe.first))
                    .forEach(x -> {
                        if (x.module.verify(components)) {
                            x.module.execute(this.container, components);
                        }
                    });
        }
        return null;
    }

    private void parseVariables(String varDefinition) {
        String[] vars = varDefinition.split(";");
        for (String a : vars) {
            String[] b = a.split("=");
            if (b.length != 2)
                throw new ParsingException("model.Variable definition must contain exactly one ':' character: " + a);
            if (b[1].charAt(0) == '\"') {//text
                if (b[1].charAt(b[1].length() - 1) == '\"' && b[1].length() >= 2) {
                    varBoxes.put(b[0], new TextVariable(b[1]));
                } else
                    throw new ParsingException("Text variable definition must contain exactly two '\"' character at front and end: " + a);
            } else if (b[1].charAt(0) == '(') {//function
                if (b[1].charAt(b[1].length() - 1) == ')') {
                    //TODO: consult whether parsing functionVariable requires further parsing or if it just accepts any string
                    varBoxes.put(b[0], new FunctionVariable(b[1]));
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
                    varBoxes.put(b[0], new MatrixVariable(dMatrix));
                } else
                    throw new ParsingException("Matrix variable definition must be within [] characters: " + a);
            } else {//numeric
                try {
                    double x = Double.parseDouble(b[1]);
                    varBoxes.put(b[0], new NumericVariable(x));
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
        String msg;

        public ParsingException(String s) {
            super(s);
            msg = s;
        }
    }

    String getSubstitutionName() {
        return getSubstitutionName(futureIndex++);
    }

    String getSubstitutionName(int index) {
        return "#" + index;
    }

    private class communicate extends DefaultTile {
        String msg;

        communicate(String msg) {
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
