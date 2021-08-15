package model;

import controllers.Parser;
import model.modules.utils.ModuleException;
import model.variables.FunctionVariable;
import model.variables.MatrixVariable;
import model.variables.NumericVariable;
import model.variables.TextVariable;
import utils.Pair;
import vartiles.DefaultTile;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.min;

// it can't be run in parallel. Should it though? Never have it been mentioned in this project, even once
//
//On the topic of more steps in interface, it makes parser depend on types returned by those steps and prevents from finding different ways of parsing
//also there would be a lot thing to return/pass that's another reason to let parser use it's internal state in calculations
public class ParserImpl implements Parser {

    public ParserImpl() {
    }

    @Override
    public TilesContainer parse(String variables, String expression) { // runs load and compute session with error handling and tile building
        ParserImplState state = new ParserImplState();
        if (variables.equals("") && expression.equals(""))
            return state.container;
        String msg;
        if ((msg = this.load(variables, expression, state)) != null) {
            state.container = new TilesContainerImpl();
            state.container.addTile(new communicate(msg).setLabel("Parsing error"));
            return state.container;
        }
        Set<String> keys = state.varBoxes.keySet();
        String[] aKeys = keys.toArray(new String[keys.size()]);
        Collections.reverse(Arrays.asList(aKeys));
        for (String key : aKeys) {
            System.out.println("variable:" + key + " " + state.varBoxes.get(key).getValue());
            state.container.addTile(new communicate(state.varBoxes.get(key).getValue().toString()).setLabel(key));
        }//after loading print variables

        try {
            this.compute(state);
        } catch (ModuleException exception) {
            state.container = new TilesContainerImpl();
            state.container.addTile(new communicate(exception.toString()).setLabel("Module error"));
        }
        return state.container;
    }


    public String load(String varDefinition, String operation, ParserImplState state) {
        //no # allowed, also remove whitespace
        varDefinition = varDefinition.replaceAll("\\s+", "").replaceAll("#", "");
        operation = operation.replaceAll("\\s+", "").replaceAll("#", "");
        try {
            this.parseVariables(varDefinition, state);
            this.simplifyOperation(operation, state);
        } catch (ParsingException e) {
            return e.msg;
        }
        return null;
    }

    public void compute(ParserImplState state) {
        int lastVar = state.futureIndex;
        state.futureIndex = 0;
        for (int i = 0; i < lastVar; ++i) {
            String varName = getSubstitutionName(i);
            Pair<String, ArrayList<String>> recipe = state.futureVariables.get(varName);

            Variable<?>[] components = new Variable[recipe.second.size()];
            for (int j = 0; j < recipe.second.size(); ++j) {
                String var = recipe.second.get(j);
                if (state.varBoxes.containsKey(var)) {
                    components[j] = state.varBoxes.get(var);
                } else {
                    state.container = new TilesContainerImpl();
                    state.container.addTile(new communicate(
                            "Could not find variable " + var + "when computing #" + i + "\n"
                    ).setLabel("Calculating error"));
                    return;
                }
            }
            AtomicBoolean foundModule = new AtomicBoolean(false); // needs to be non-primitive type to work in lambda
            EnumSet.allOf(Modules.class)
                    .stream()
                    .filter(x -> x.name.equals(recipe.first))
                    .forEach((x) -> {
                        Module<?> module = x.module;
                        if (module.verify(components) && !foundModule.get()) {
                            Variable<?> got = module.execute(state.container, components);
                            state.varBoxes.put(varName, got);
                            String result = String.format("%s(%s)=%s\n",
                                    recipe.first,
                                    String.join(",", recipe.second),
                                    got.getValue().toString());
                            state.container.addTile(new communicate(result).setLabel(varName));
                            foundModule.set(true);
                        }
                    });

            EnumSet.allOf(TerminalModules.class)
                    .stream()
                    .filter(x -> x.name.equals(recipe.first))
                    .forEach(x -> {
                        if (x.module.verify(components) && !foundModule.get()) {
                            x.module.execute(state.container, components);
                            foundModule.set(true);
                        }
                    });
            if (!foundModule.get()) {
                state.container = new TilesContainerImpl();
                state.container.addTile(new communicate(
                        "Couldn't find possible operation associated with " + recipe.first + " to obtain " + varName + "\n"
                ).setLabel("Calculating error"));
                return;
            }
        }
        return;
    }

    private void parseVariables(String varDefinition, ParserImplState state) {
        String[] vars = varDefinition.split(";");
        for (String a : vars) {
            String[] b = a.split("=");
            if (b.length != 2)
                throw new ParsingException("model.Variable definition must contain exactly one ':' character: " + a);
            if (b[1].charAt(0) == '\"') {//text
                if (b[1].charAt(b[1].length() - 1) == '\"' && b[1].length() >= 2) {
                    state.varBoxes.put(b[0], new TextVariable(b[1]));
                } else
                    throw new ParsingException("Text variable definition must contain exactly two '\"' character at front and end: " + a);
            } else if (b[1].charAt(0) == '(') {//function
                if (b[1].charAt(b[1].length() - 1) == ')') {
                    //TODO: consult whether parsing functionVariable requires further parsing or if it just accepts any string
                    state.varBoxes.put(b[0], new FunctionVariable(b[1]));
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
                    state.varBoxes.put(b[0], new MatrixVariable(dMatrix));
                } else
                    throw new ParsingException("Matrix variable definition must be within [] characters: " + a);
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

    private void simplifyOperation(String operation, ParserImplState state) {
        ArrayList<String> list = new ArrayList<>();
        simplify(operation, list, state);
    }

    public static class ParsingException extends IllegalArgumentException {
        String msg;

        public ParsingException(String s) {
            super(s);
            msg = s;
        }
    }

    String getSubstitutionName(ParserImplState state) {
        return getSubstitutionName(state.futureIndex++);
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

    private String simplify(String query, ArrayList<String> list, ParserImplState state) {
        char[] chars = query.toCharArray();
        int i = 0;
        while (i < query.length()) {
            if (chars[i] == '(') {//a bit like ,
                String operation = query.substring(0, i);
                ArrayList<String> myVars = new ArrayList<>();
                query = simplify(query.substring(min(i + 1, query.length())), myVars, state);
                String varName = getSubstitutionName(state);
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
