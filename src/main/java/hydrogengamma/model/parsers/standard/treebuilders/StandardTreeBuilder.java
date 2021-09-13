package hydrogengamma.model.parsers.standard.treebuilders;

import hydrogengamma.model.parsers.standard.Expression;
import hydrogengamma.model.parsers.standard.ParsingException;
import hydrogengamma.model.parsers.standard.TreeBuilder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StandardTreeBuilder implements TreeBuilder {
    private static final Logger logger = Logger.getLogger(StandardTreeBuilder.class);
    private final Map<String, Character> signFixes;
    private final ArrayList<ArrayList<Character>> signHierarchy;

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

    private String removeWhitespace(String operation) {
        return operation.replaceAll("\\s+", "").replaceAll("#", "");
    }

    @Override
    public List<Expression> build(String operation) {
        List<Expression> expressions = new ArrayList<>();
        if (operation.equals(""))
            return expressions;
        operation = removeWhitespace(operation);
        logger.debug("Passed operation:" + operation + ":");
        operation = fixSigns(operation);
        Integer[] nextIndex = new Integer[1];
        nextIndex[0] = 0;
        this.simplifyOperation(operation, expressions, nextIndex);
        return expressions;
    }

    private String fixSigns(String operation) {
        logger.debug("Prefixing: " + operation);
        boolean changed;
        do {
            changed = false;
            char[] chars = operation.toCharArray();
            for (int i = 1; i < chars.length; ++i) {
                final String key = String.format("%s%s", chars[i - 1], chars[i]);
                if (signFixes.containsKey(key)) {
                    chars[i] = signFixes.get(key);
                    chars[i - 1] = ' ';
                    changed = true;
                }
            }
            operation = String.valueOf(chars).replaceAll("\\s", "");
        } while (changed);
        logger.debug("Postfix: " + operation);
        return operation;
    }

    private String simplifyOperation(String operation, List<Expression> expressions, Integer[] nextIndex) {
        logger.debug("attempting to parse: " + operation);
        for (ArrayList<Character> signs : signHierarchy) {
            for (Character sign : signs) {
                int depth = 0;
                for (int i = operation.length() - 1; i >= 0; --i) {
                    if (operation.charAt(i) == '(')
                        depth -= 1;
                    else if (operation.charAt(i) == ')')
                        depth += 1;
                    else if (depth == 0 && operation.charAt(i) == sign) {
                        return substituteVariableAsSignOperation(operation, expressions, nextIndex, i);
                    }
                }
            }
        }
        //getting here means there is no sign on current level so this is one parenthesised operation or simple variable (without signs)
        if (operation.matches("[a-zA-Z]*[(].*[)]")) {
            return substitutePrefixOperation(operation, expressions, nextIndex);
        }
        if (operation.contains("(") || operation.contains(")"))
            throw new ParsingException("mismatched parenthesis in " + operation);
        if (operation.contains(","))
            throw new ParsingException("unexpected comma (,) in " + operation);
        return operation;
    }

    private String substitutePrefixOperation(String operation, List<Expression> expressions, Integer[] nextIndex) {
        logger.debug("matched:" + operation + " as a simple prefix module");
        String newName = getSubstitutionName(nextIndex);
        ArrayList<String> subExpressions = new ArrayList<>();
        String bodyOfFunction = operation.substring(operation.indexOf('(') + 1, operation.lastIndexOf(')'));
        List<String> arguments = splitIntoArguments(bodyOfFunction);
        logger.debug(bodyOfFunction + " from " + operation + " was divided into: " + arguments);
        for (String arg : arguments) {
            subExpressions.add(simplifyOperation(arg, expressions, nextIndex));
        }
        Expression local = new Expression(newName, operation, operation.substring(0, operation.indexOf('(')), subExpressions);
        expressions.add(local);
        logger.debug("created: " + local + " from: " + operation);
        return newName;
    }

    private String substituteVariableAsSignOperation(String operation, List<Expression> expressions, Integer[] nextIndex, int i) {
        int leftIndex = 0;
        int rightIndex = operation.length();
        String left = operation.substring(leftIndex, i);
        String right = operation.substring(i + 1, rightIndex);
        ArrayList<String> subExpressions = new ArrayList<>();
        String functionName = String.valueOf(operation.charAt(i));
        logger.debug(" initial :" + operation + ": was divided into :" + left + ": :" + functionName + ": :" + right + ":");
        if (left.length() > 0)
            subExpressions.add(simplifyOperation(left, expressions, nextIndex));
        if (right.length() > 0)
            subExpressions.add(simplifyOperation(right, expressions, nextIndex));
        String newName = getSubstitutionName(nextIndex);
        Expression local = new Expression(newName, operation, functionName, subExpressions);
        expressions.add(local);
        logger.debug("created: " + local + " from: " + operation);
        return simplifyOperation(operation.substring(0, leftIndex) + newName + operation.substring(rightIndex), expressions, nextIndex);
    }

    public String getSubstitutionName(Integer[] index) {
        index[0] += 1;
        return (index[0] - 1) + "var";
    }

    private List<String> splitIntoArguments(String body) {
        ArrayList<String> arguments = new ArrayList<>();
        int i = 0;
        int depth = 0;
        for (int j = 0; j < body.length(); ++j) {
            if (body.charAt(j) == '(')
                depth += 1;
            else if (body.charAt(j) == ')')
                depth -= 1;
            else if (body.charAt(j) == ',' && depth == 0) {
                arguments.add(body.substring(i, j));
                i = j + 1;
            }
        }
        if (i <= body.length() - 1)
            arguments.add(body.substring(i));
        return arguments;
    }

}

