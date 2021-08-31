package hydrogengamma.model.parsers.standard;

import hydrogengamma.controllers.Expression;
import hydrogengamma.controllers.TreeBuilder;
import hydrogengamma.utils.Pair;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class StandardTreeBuilder implements TreeBuilder {
    @Override
    public List<Expression> build(String operation) {
        List<Expression> expressions = new ArrayList<>();
        operation = "(" + operation.replaceAll("\\s+", "").replaceAll("#", "") + ")";
        System.out.println("operation:" + operation + ":");
        operation = fixSigns(operation);
        this.simplifyOperation(operation, expressions);
        return expressions;
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

    private void simplifyOperation(String operation, List<Expression> expressions) {
        ArrayList<String> list = new ArrayList<>();
        simplify(operation, list, expressions);
    }

    private String simplify(String query, ArrayList<String> list, List<Expression> expressions) {
        Integer nextIndex = 0;
        char[] chars = query.toCharArray();
        int i = 0;
        while (i < query.length()) {
            if (chars[i] == '(') {//a bit like ,
                String operation = query.substring(0, i);
                ArrayList<String> myVars = new ArrayList<>();
                query = simplify(query.substring(min(i + 1, query.length())), myVars, expressions);
                String varName = resolveAndAddFuture(new Pair<>(operation, myVars), expressions, nextIndex);
                list.add(varName);
                return simplify(query, list, expressions);
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
                return simplify(query, list, expressions);
            }
            ++i;
        }
        return "";
    }

    private String resolveAndAddFuture(Pair<String, ArrayList<String>> definition, List<Expression> expressions, Integer nextIndex) {
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
                        resolved.second.add(resolveAndAddFuture(tmp, expressions, nextIndex));
                    } else {
                        tmp.second.add(var.substring(0, pos));
                        tmp.second.add(var.substring(pos + 1));
                        System.out.println(var.charAt(pos) + " ( " + var.substring(0, pos) + " , " + var.substring(pos + 1) + " )");
                        resolved.second.add(resolveAndAddFuture(tmp, expressions, nextIndex));
                    }
                } else if ((pos = max(var.indexOf('*'), var.indexOf('/'))) != -1) {
                    Pair<String, ArrayList<String>> tmp = new Pair<>(Character.toString(var.charAt(pos)), new ArrayList<>());
                    if (pos == 0) {
                        tmp.second.add(var.substring(1));
                        System.out.println(var.charAt(pos) + " ( " + var.substring(1) + " )");
                        resolved.second.add(resolveAndAddFuture(tmp, expressions, nextIndex));
                    } else {
                        tmp.second.add(var.substring(0, pos));
                        tmp.second.add(var.substring(pos + 1));
                        System.out.println(var.charAt(pos) + " ( " + var.substring(0, pos) + " , " + var.substring(pos + 1) + " )");
                        resolved.second.add(resolveAndAddFuture(tmp, expressions, nextIndex));
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
                String new_var = resolveAndAddFuture(resolved, expressions, nextIndex);
                resolved = new Pair<>("", new ArrayList<>());
                resolved.first = "";
                resolved.second.add(definition.first + new_var);
                System.out.println("( " + definition.first + new_var + " ) where " + new_var + " = ( inside )");
                return resolveAndAddFuture(resolved, expressions, nextIndex);

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
                String new_var = resolveAndAddFuture(resolved, expressions, nextIndex);
                resolved = new Pair<>("", new ArrayList<>());
                resolved.first = "";
                resolved.second.add(definition.first.substring(0, definition.first.length() - 1 - i) + new_var);
                System.out.println("( " + definition.first.substring(0, definition.first.length() - 1 - i) + new_var + " ) where " + new_var + " = " + definition.first.substring(definition.first.length() - 1 - i) + "( inside )");
                return resolveAndAddFuture(resolved, expressions, nextIndex);
            }
        }
        String varName = this.getSubstitutionName(++nextIndex);
        expressions.add(new Expression(varName, null, resolved.first, resolved.second));//TODO: make null a real label from user input (should be pushed from top? and edited in fly) MICHAL
        return varName;
    }

    public String getSubstitutionName(Integer index) {
        return index + "var";
    }

}
