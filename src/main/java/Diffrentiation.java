import util.Pair;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

public abstract class Diffrentiation {

    public static TreeMap<String, String> knownDerivatives = new TreeMap<>();
    public static TreeSet<Character> knownOperators = new TreeSet<>();

    static {
        knownDerivatives.put("sin(x)", "cos(x)");
        knownDerivatives.put("cos(x)", "(-sin(x))");
        knownDerivatives.put("e^(x)", "e^(x)");
        knownDerivatives.put("x", "1");
        knownDerivatives.put("1", "0");
        knownDerivatives.put("0", "0");
        knownDerivatives.put("ln(x)", "1/x");

        knownOperators.add('+');
        knownOperators.add('-');
        knownOperators.add('*');
        knownOperators.add('/');
    }

    public static FunctionVariable symbolicDerivative(FunctionVariable func) {
        String formula = removeParentheses(func.value);

        if (formula.isEmpty())
            throw new InvalidFormulaException(formula);

        if (isTrivial(formula))
            return new FunctionVariable(knownDerivatives.get(formula));

        if (formula.charAt(0) == '-' && isTrivial(formula.substring(1)))
            return new FunctionVariable('-' + knownDerivatives.get(formula.substring(1)));


        formula = removeParentheses(formula);

        ArrayList<Integer> operPos = operatorsPositions(formula);

        if (operPos.isEmpty())
            throw new DerivativeNotKnownException(formula);
        if (!correctOperators(formula))
            throw new InvalidFormulaException(formula);

        StringBuilder derivativeFormula = new StringBuilder();
        Pair<ArrayList<String>, ArrayList<Character>> additive = findSubcomponents(formula, "+-");

        if (additive.second.isEmpty()) {    // multiplicative
            Pair<ArrayList<String>, ArrayList<Character>> multiplicative = findSubcomponents(formula, "*/");
            if (multiplicative.second.isEmpty())
                throw new DerivativeNotKnownException(formula);
            ArrayList<String> components = multiplicative.first;
            ArrayList<Character> operators = multiplicative.second;

            derivativeFormula = new StringBuilder(removeParentheses(components.get(0)));

            for (int i = 1; i < components.size(); i++) {
                String nextComp = components.get(i);

                String buffDeriv = symbolicDerivative(new FunctionVariable(derivativeFormula.toString())).value;
                String nextDeriv = symbolicDerivative(new FunctionVariable(nextComp)).value;

                if (operators.get(i - 1) == '*') {
                    derivativeFormula.insert(0, '(');
                    derivativeFormula.append('*');
                    derivativeFormula.append(nextDeriv);
                    derivativeFormula.append('+');
                    derivativeFormula.append(removeParentheses(nextComp));
                    derivativeFormula.append('*');
                    derivativeFormula.append(buffDeriv);
                    derivativeFormula.append(')');
                }
                else {  // oper == '/'
                    derivativeFormula.insert(0, '(');
                    derivativeFormula.append("*(-1)*");
                    derivativeFormula.append(nextDeriv);
                    derivativeFormula.append("/(");
                    derivativeFormula.append(removeParentheses(nextComp));
                    derivativeFormula.append(")^2");

                    derivativeFormula.append('+');
                    derivativeFormula.append(removeParentheses(nextComp));
                    derivativeFormula.append('*');
                    derivativeFormula.append(buffDeriv);
                    derivativeFormula.append("/(");
                    derivativeFormula.append(removeParentheses(nextComp));
                    derivativeFormula.append(")^2");
                    derivativeFormula.append(')');
                }
            }
        }
        else {
            derivativeFormula.insert(0, '(');
            ArrayList<String> components = additive.first;
            ArrayList<Character> operators = additive.second;

            for (int i = 0; i < operators.size(); i++) {
                FunctionVariable compDeriv = symbolicDerivative(new FunctionVariable(components.get(i)));
                derivativeFormula.append(compDeriv.value);
                derivativeFormula.append(operators.get(i));
            }
            derivativeFormula.append(symbolicDerivative(new FunctionVariable(components.get(components.size() - 1))).value);
            derivativeFormula.append(')');
        }

        return new FunctionVariable(derivativeFormula.toString());
    }

    private static Pair<ArrayList<String>, ArrayList<Character>> findSubcomponents(String formula, String searchedOpers) {
        ArrayList<String> components = new ArrayList<>();
        ArrayList<Character> operations = new ArrayList<>();
        int opened = 0;

        if (formula.charAt(0) == '(')
            opened++;

        int prev = 0;
        for (int i = 1; i < formula.length(); i++) {
            if (formula.charAt(i) == '(')
                opened++;
            if (formula.charAt(i) == ')')
                opened--;
            if (searchedOpers.indexOf(formula.charAt(i)) != -1 && opened == 0) {
                components.add(formula.substring(prev, i));
                operations.add(formula.charAt(i));
                prev = i + 1;
            }
        }

        components.add(formula.substring(prev, formula.length()));
        return new Pair<>(components, operations);
    }

    // TODO
    private static String removeParentheses(String formula) {
        int n = formula.length();
        if (formula.length() < 2 || formula.charAt(0) != '(' || formula.charAt(n - 1) != ')')
            return formula;

        Stack<Integer> opening = new Stack<>();

        int preflast = 0;
        while (preflast < n && formula.charAt(preflast) == '(')
            preflast++;
        preflast--;

        int suffirst = n - 1;
        while (suffirst >= 0 && formula.charAt(suffirst) == ')')
            suffirst--;
        suffirst++;

        int beginIdx = 0;
        int finIdx = n;

        for (int i = 0; i < n; i++) {
            if (formula.charAt(i) == '(')
                opening.push(i);
            if (formula.charAt(i) == ')') {
                if (i >= suffirst && opening.peek() <= preflast) {
                    beginIdx = opening.peek() + 1;
                    finIdx = i;
                    break;
                }
                opening.pop();
            }
        }

        return formula.substring(beginIdx, finIdx);
    }

    private static boolean isTrivial(String formula) {
        return knownDerivatives.containsKey(formula);
    }

    private static ArrayList<Integer> operatorsPositions(String formula) {
        int opened = 0;
        ArrayList<Integer> operators = new ArrayList<>();
        for (int i = 0; i < formula.length(); i++) {
            if (formula.charAt(i) == '(')
                opened++;
            if (formula.charAt(i) == ')')
                opened--;

            if (knownOperators.contains(formula.charAt(i)) && opened == 0)
                operators.add(i);
        }

        return operators;
    }

    private static boolean correctOperators(String formula) {
        if (formula.charAt(0) == '*' || formula.charAt(0) == '/' || knownOperators.contains(formula.charAt(formula.length() - 1)))
            return false;

        for (int i = 0; i < formula.length() - 1; i++)
            if (knownOperators.contains(formula.charAt(i)) && knownOperators.contains(formula.charAt(i + 1)))
                return false;
        return true;
    }

    public static class DerivativeNotKnownException extends RuntimeException {
        private final String formula;
        DerivativeNotKnownException(String form) {
            formula = form;
        }
        @Override
        public String toString() {
            return formula;
        }
    }
    public static class InvalidFormulaException extends RuntimeException {
        private final String formula;
        InvalidFormulaException(String form) {
            formula = form;
        }
        @Override
        public String toString() {
            return formula;
        }
    }
}
