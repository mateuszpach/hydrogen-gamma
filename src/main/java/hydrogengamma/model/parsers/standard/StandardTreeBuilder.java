package hydrogengamma.model.parsers.standard;

import hydrogengamma.controllers.Expression;
import hydrogengamma.controllers.TreeBuilder;
import hydrogengamma.utils.Pair;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class StandardTreeBuilder implements TreeBuilder {
    private final Map<String, Character> signFixes;
    private final ArrayList<ArrayList<Character>> signHierarchy;

    private static final Logger logger = Logger.getLogger(StandardTreeBuilder.class);

    public StandardTreeBuilder() {
        this.signFixes = new TreeMap<>();
        {
            this.signFixes.put("++", '+');
            this.signFixes.put("-+", '-');
            this.signFixes.put("+-", '-');
            this.signFixes.put("--", '+');
            this.signFixes.put("*+", '*');
            this.signFixes.put("/+", '/');
        }
        this.signHierarchy = new ArrayList<>();
        {
            ArrayList<Character> signs;
            signs = new ArrayList<>();
            signs.add('&');
            signs.add('|');
            signs.add('^');
            this.signHierarchy.add(signs);
            signs = new ArrayList<>();
            signs.add('+');
            signs.add('-');
            this.signHierarchy.add(signs);
            signs = new ArrayList<>();
            signs.add('*');
            signs.add('/');
            this.signHierarchy.add(signs);
        }
    }

    @Override
    public List<Expression> build(String operation) {
        List<Expression> expressions = new ArrayList<>();
        if (operation.equals(""))
            return expressions;
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
        while (changed) {
            //seems dumb, might be dumb, is "ineffective", BUT simple and bulletproof
            // using some sort string.contains does not improve complexity, either way while should run twice (is left for some weird unforeseen corner case)
            changed = false;
            out = new StringBuilder();
            char[] chars = operation.toCharArray();
            for (int i = 1; i < chars.length; ++i) {
                final String key = chars[i - 1] + String.valueOf(chars[i]);
                if (signFixes.containsKey(key)) {
                    chars[i] = signFixes.get(key);
                    chars[i - 1] = ' ';
                    changed = true;
                }
            }
            for (char x : chars)
                if (x != ' ') out.append(x);
            operation = out.toString();
        }
        System.out.println("post fixing: " + operation);
        return operation;
    }

    private void simplifyOperation(String operation, List<Expression> expressions) {
        ArrayList<String> list = new ArrayList<>();
        Integer[] nextIndex = new Integer[1];
        nextIndex[0] = 0;
        Map<String, String> variablesText = new TreeMap<>();
        simplify(operation, list, expressions, variablesText, nextIndex);
    }

    private String simplify(String query, ArrayList<String> list, List<Expression> expressions, Map<String, String> variablesText, Integer[] nextIndex) {
        char[] chars = query.toCharArray();
        int i = 0;
        while (i < query.length()) {
            if (chars[i] == '(') {//a bit like ,
                String operation = query.substring(0, i);
                ArrayList<String> myVars = new ArrayList<>();
                query = simplify(query.substring(min(i + 1, query.length())), myVars, expressions, variablesText, nextIndex);
                String varName = resolveSignsAndCreateExpression(new Pair<>(operation, myVars), expressions, variablesText, nextIndex);
                list.add(varName);
                return simplify(query, list, expressions, variablesText, nextIndex);
            } else if (chars[i] == ')' || chars[i] == ',' || i + 1 == chars.length) {//go up, and substitute or add and continue
                String var = query.substring(0, i);
                if (var.length() > 0)
                    list.add(var);
                if (i + 1 == query.length())
                    query = "";
                else {
                    query = query.substring(i + 1);
                }
                if (chars[i] == ')')
                    return query;
                else
                    return simplify(query, list, expressions, variablesText, nextIndex);
            }
            ++i;
        }
        return "";
    }

    private int getSignIndex(String var) {
        int pos = -1;
        for (ArrayList<Character> signs : this.signHierarchy) {
            for (Character sign : signs)
                pos = max(pos, var.indexOf(sign));
            if (pos != -1)
                break;
        }
        return pos;
    }

    private String resolveSignsAndCreateExpression(Pair<String, ArrayList<String>> definition, List<Expression> expressions, Map<String, String> variablesText, Integer[] nextIndex) {
        Pair<String, ArrayList<String>> resolved = new Pair<>("", new ArrayList<>());
        for (String var : definition.second) {
            logger.debug("Expression to resolve: " + var + "    resolved to: ");
            resolveSignsInModuleArgument(expressions, variablesText, nextIndex, resolved, var);
        }
        logger.debug("Resolving operation name from: " + definition.first + "   to: ");
        if (definition.first.matches("\\w*") || definition.first.matches("\\W$")) {// is simple definition of just mod name or just sign
            resolved.first = definition.first;
        } else {//has operation
            return resolveSignsInModuleName(definition, expressions, variablesText, nextIndex, resolved);
        }
        String varName = this.getSubstitutionName(nextIndex[0]++);
        String expressionText = getExpressionText(resolved.first, resolved.second, variablesText);
        variablesText.put(varName, expressionText);
        expressions.add(new Expression(varName, expressionText, resolved.first, resolved.second));
        logger.debug("Resolver created: " + expressions.get(expressions.size() - 1));
        return varName;
    }

    private String resolveSignsInModuleName(Pair<String, ArrayList<String>> definition, List<Expression> expressions, Map<String, String> variablesText, Integer[] nextIndex, Pair<String, ArrayList<String>> resolved) {
        if (definition.first.matches(".*\\W$")) {//ends with special sign
            resolved.first = "";
            String new_var = resolveSignsAndCreateExpression(resolved, expressions, variablesText, nextIndex);
            resolved = new Pair<>("", new ArrayList<>());
            resolved.first = "";
            resolved.second.add(definition.first + new_var);
        } else {// ends with modular operation
            System.out.println("case: modular");
            int i = 0;
            while (i < definition.first.length() && definition.first.substring(definition.first.length() - 1 - i).matches("\\w*")) { // get the longest trailing valid word
                ++i;
            }
            --i;
            resolved.first = definition.first.substring(definition.first.length() - 1 - i);
            String new_var = resolveSignsAndCreateExpression(resolved, expressions, variablesText, nextIndex);
            resolved = new Pair<>("", new ArrayList<>());
            resolved.first = "";
            resolved.second.add(definition.first.substring(0, definition.first.length() - 1 - i) + new_var);
            System.out.println("( " + definition.first.substring(0, definition.first.length() - 1 - i) + new_var + " ) where " + new_var + " = " + definition.first.substring(definition.first.length() - 1 - i) + "( inside )");
        }
        return resolveSignsAndCreateExpression(resolved, expressions, variablesText, nextIndex);
    }

    private void resolveSignsInModuleArgument(List<Expression> expressions, Map<String, String> variablesText, Integer[] nextIndex, Pair<String, ArrayList<String>> resolved, String var) {
        if (!var.matches("\\w*")) {//contains sign
            int pos;
            if ((pos = getSignIndex(var)) != -1) {
                Pair<String, ArrayList<String>> tmp = new Pair<>(Character.toString(var.charAt(pos)), new ArrayList<>());
                if (pos == 0) {
                    tmp.second.add(var.substring(1));
                    System.out.println(var.charAt(pos) + " ( " + var.substring(1) + " )");
                } else {
                    tmp.second.add(var.substring(0, pos));
                    tmp.second.add(var.substring(pos + 1));
                    System.out.println(var.charAt(pos) + " ( " + var.substring(0, pos) + " , " + var.substring(pos + 1) + " )");
                }
                resolved.second.add(resolveSignsAndCreateExpression(tmp, expressions, variablesText, nextIndex));
            }
        } else {
            System.out.println("just " + var);
            resolved.second.add(var);
        }
    }

    public String getSubstitutionName(Integer index) {
        return index + "var";
    }

    private String getExpressionText(String functionName, ArrayList<String> subexpressionsTexts, Map<String, String> variablesText) {
        ArrayList<String> translatedNames = new ArrayList<>();
        for (String str : subexpressionsTexts) {
            translatedNames.add(variablesText.getOrDefault(str, str));
        }
        if (functionName.matches("\\W")) {
            return String.join(functionName, translatedNames);
        } else {
            return String.format("%s(%s)",
                    functionName,
                    String.join(", ", translatedNames));
        }
    }

}
