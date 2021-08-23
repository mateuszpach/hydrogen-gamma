package hydrogengamma.model.parsers.standard;

import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.variables.TextVariable;
import hydrogengamma.utils.Pair;

import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Loader {

    public State load(String varDefinition, String operation) {
        State state = new State();
        //no # allowed, also remove whitespace
        varDefinition = varDefinition.replaceAll("#", "");
        operation = "(" + operation.replaceAll("\\s+", "").replaceAll("#", "") + ")";
        System.out.println("var:" + varDefinition + ": operation:" + operation + ":");
        this.parseVariables(varDefinition, state);
        operation = fixSigns(operation);
        operation = this.replaceConstants(operation, state);
        this.simplifyOperation(operation, state);
        this.flattenTree(state);
        return state;
    }

    private void flattenTree(State state) {
        //  Map<String, Pair<String, ArrayList<String>>> flat = new HashMap<>();
        //  int top=state.futureIndex-1;

    }

    private String fixSigns(String operation) {
        StringBuilder out;
        System.out.println("pre fixing: " + operation);
        boolean changed = true;
        while (changed) {//seems dumb, might be dumb, is ineffective, BUT simple and bulletproof, when user input WON'T be too long
            changed = false;
            out = new StringBuilder();
            char[] chars = operation.toCharArray();
            for (int i = 1; i < chars.length; ++i) {
                if (chars[i - 1] == '+' && chars[i] == '+') {
                    chars[i - 1] = ' ';
                    chars[i] = '+';
                    changed = true;
                } else if (chars[i - 1] == '+' && chars[i] == '-') {
                    chars[i - 1] = ' ';
                    chars[i] = '-';
                    changed = true;
                } else if (chars[i - 1] == '-' && chars[i] == '+') {
                    chars[i - 1] = ' ';
                    chars[i] = '-';
                    changed = true;
                } else if (chars[i - 1] == '-' && chars[i] == '-') {
                    chars[i - 1] = ' ';
                    chars[i] = '+';
                    changed = true;
                } else if (chars[i - 1] == '*' && chars[i] == '+') {
                    chars[i - 1] = ' ';
                    chars[i] = '*';
                    changed = true;
                } else if (chars[i - 1] == '/' && chars[i] == '+') {
                    chars[i - 1] = ' ';
                    chars[i] = '/';
                    changed = true;
                }
            }
            for (char x : chars) {
                if (x != ' ')
                    out.append(x);
            }
            operation = out.toString();
        }
        System.out.println("post fixing: " + operation);
        return operation;
    }

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
                        // break here would parse "500" into 3 variables 5,0,0, while 'legit' check makes sure stranded '-' isn't being attempted to be parsed as number

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

    // TODO Extractors dla parsera zwracające odpowiednie typy varaible LUKASZ
    // note: varDefinition will contain whitespace, remove it from everywhere BUT textVariable content (don't leave it in variables names)
    private void parseVariables(String varDefinition, State state) {
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
                    state.addExpression(b[0], new State.Expression(
                            b[0]
                            , new TextVariable(b[1].substring(1, b[1].length() - 1))));
                } else
                    throw new ParsingException("Text variable definition must be within '\"\"' quotation: " + a);
            } else {
                b[1] = b[1].replaceAll("\\s+", "");// not a text so remove all whitespace left
                if (b[1].charAt(0) == '(') {//function
                    if (b[1].charAt(b[1].length() - 1) == ')') {
                        state.addExpression(b[0], new State.Expression(
                                b[0]
                                , new FunctionVariable(b[1])));
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
                        state.addExpression(b[0], new State.Expression(
                                b[0],
                                new MatrixVariable(dMatrix)));
                    } else
                        throw new ParsingException("Matrix variable definition must be within [] parenthesis: " + a);
                } else {//numeric
                    try {
                        double x = Double.parseDouble(b[1]);
                        state.addExpression(b[0], new State.Expression(
                                b[0],
                                new NumericVariable(x)));
                    } catch (NumberFormatException e) {
                        throw new ParsingException(b[1] + " from definition " + a + " does not represent valid number");
                    }
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
                String varName = resolveAndAddFuture(new Pair<>(operation, myVars), state);
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

    private String resolveAndAddFuture(Pair<String, ArrayList<String>> definition, State state) {
        //1. innards don't have parenthesis! just resolve bioperators
        //new rule if some uses a*-b or a*-(...) it will explode, be nice and write a*(-b) or a*(-(...))
        Pair<String, ArrayList<String>> resolved = new Pair<>("", new ArrayList<>());
        for (String var : definition.second) {
            System.out.print("delinquent to resolve: " + var + "    resolved to: ");
            if (!var.matches("\\w*")) {//contains sign
                //find *,/ and reorder a*b into *(a,b), don't look at anything else
                //resolving from lower to higher order will push low order operations outward, so they will execute last
                //+- then */ recursively
                //what if +(a+b+c,d+e+f) resolved as a+b+n_var+e+f, n_var=c+d ? pointless complication, just don't
                // a*b+c*d => +(a*b,c*d) => +(*(a,b),*(c,d))
                // a+b*c+d => +(a,b*c+d) => +(a,+(b*c,d)) => +(a,+(*(b,c),d))
                int pos;
                if ((pos = max(var.indexOf('+'), var.indexOf('-'))) != -1) {
                    Pair<String, ArrayList<String>> tmp = new Pair<>(Character.toString(var.charAt(pos)), new ArrayList<>());
                    if (pos == 0) {
                        tmp.second.add(var.substring(1));
                        System.out.println(var.charAt(pos) + " ( " + var.substring(1) + " )");
                        resolved.second.add(resolveAndAddFuture(tmp, state));
                    } else {
                        tmp.second.add(var.substring(0, pos));
                        tmp.second.add(var.substring(pos + 1));
                        System.out.println(var.charAt(pos) + " ( " + var.substring(0, pos) + " , " + var.substring(pos + 1) + " )");
                        resolved.second.add(resolveAndAddFuture(tmp, state));
                    }
                } else if ((pos = max(var.indexOf('*'), var.indexOf('/'))) != -1) {
                    Pair<String, ArrayList<String>> tmp = new Pair<>(Character.toString(var.charAt(pos)), new ArrayList<>());
                    if (pos == 0) {
                        tmp.second.add(var.substring(1));
                        System.out.println(var.charAt(pos) + " ( " + var.substring(1) + " )");
                        resolved.second.add(resolveAndAddFuture(tmp, state));
                    } else {
                        tmp.second.add(var.substring(0, pos));
                        tmp.second.add(var.substring(pos + 1));
                        System.out.println(var.charAt(pos) + " ( " + var.substring(0, pos) + " , " + var.substring(pos + 1) + " )");
                        resolved.second.add(resolveAndAddFuture(tmp, state));
                    }
                }
            } else {
                System.out.println("just " + var);
                resolved.second.add(var);
            }
        }
        System.out.println("resolving operation name from: " + definition.first + "   to: ");
        if (definition.first.matches("\\w*") || definition.first.matches("\\W$")) {// is simple definition of just mod name or just sign
            System.out.println("case: simple");
            resolved.first = definition.first;
            System.out.println("just " + resolved.first);
        } else {//has operation
            if (definition.first.matches(".*\\W$")) {//ends with special sign
                System.out.println("case: special");
                // a+b*(c) => ret (a+b*new_var), new_var=(c)
                // a*b+(c) => ret (a*b+new_var), new_var=(c)
                resolved.first = "";
                String new_var = resolveAndAddFuture(resolved, state);
                resolved = new Pair<>("", new ArrayList<>());
                resolved.first = "";
                resolved.second.add(definition.first + new_var);
                System.out.println("( " + definition.first + new_var + " ) where " + new_var + " = ( inside )");
                return resolveAndAddFuture(resolved, state);

            } else {// ends with modular operation
                System.out.println("case: modular");
                int i = 0;
                while (i < definition.first.length() && definition.first.substring(definition.first.length() - 1 - i).matches("\\w*")) { // get the longest trailing valid word
                    ++i;
                    System.out.println("i: " + i + "   :   " + definition.first.substring(0, definition.first.length() - 1 - i) + " " + definition.first.substring(definition.first.length() - 1 - i));
                }
                --i;
                // a+b*mod(c,d) => ret (a+b*new_var), new_var=mod(c,d)
                // a*b+mod(c,d) => ret (a*b+new_var), new_var=mod(c,d)
                resolved.first = definition.first.substring(definition.first.length() - 1 - i);
                String new_var = resolveAndAddFuture(resolved, state);
                resolved = new Pair<>("", new ArrayList<>());
                resolved.first = "";
                resolved.second.add(definition.first.substring(0, definition.first.length() - 1 - i) + new_var);
                System.out.println("( " + definition.first.substring(0, definition.first.length() - 1 - i) + new_var + " ) where " + new_var + " = " + definition.first.substring(definition.first.length() - 1 - i) + "( inside )");
                return resolveAndAddFuture(resolved, state);
            }
        }
        String varName = state.getSubstitutionName();
        state.addExpression(varName, new State.Expression(resolved.first, resolved.second));
        return varName;
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
