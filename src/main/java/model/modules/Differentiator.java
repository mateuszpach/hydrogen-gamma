package model.modules;

import model.Module;
import model.TilesContainer;
import model.Variable;
import model.modules.utils.Functions;
import utils.Pair;
import model.variables.FunctionVariable;
import vartiles.FunctionTile;
import vartiles.factories.FunctionTileFactory;

import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

public class Differentiator implements Module<FunctionVariable> {

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

    @Override
    public FunctionVariable execute(TilesContainer container, Variable<?>... args) {
        FunctionVariable function = (FunctionVariable) args[0];
        container.addTile(new FunctionTileFactory().get(function, "derivative"));
        return symbolicDerivative(function);
    }

    private static FunctionVariable symbolicDerivative(FunctionVariable func) {
        String formula = removeParentheses(func.getValue());

        if (formula.isEmpty())
            throw new InvalidFormulaException(formula);

        formula = removeParentheses(formula);
        if (isConstant(formula))
            return new FunctionVariable("0");

        StringBuilder derivativeFormula = new StringBuilder();
        boolean negative = false;

        if (formula.charAt(0) == '-') {
            negative = true;
            formula = formula.substring(1, formula.length());
        }
        formula = removeParentheses(formula);

        boolean done = false;

        if (isTrivial(formula)) {
            done = true;
            derivativeFormula.append(knownDerivatives.get(formula));
        }
        else if (isPoly(formula)) {
            done = true;
            derivativeFormula.append(polyDeriv(formula));
        }

        if (done) {
            if (negative) {
                derivativeFormula.insert(0, "(-(");
                derivativeFormula.append("))");
            }
            return new FunctionVariable(derivativeFormula.toString());
        }

        ArrayList<Integer> operPos = operatorsPositions(formula);

        if (operPos.isEmpty())
            throw new DerivativeNotKnownException(formula);
        if (!correctOperators(formula))
            throw new InvalidFormulaException(formula);

        Pair<ArrayList<String>, ArrayList<Character>> additive = Functions.findSubcomponents(formula, "+-");

        if (additive.second.isEmpty()) {    // multiplicative
            Pair<ArrayList<String>, ArrayList<Character>> multiplicative = Functions.findSubcomponents(formula, "*/");
            if (multiplicative.second.isEmpty())
                throw new DerivativeNotKnownException(formula);
            ArrayList<String> components = multiplicative.first;
            ArrayList<Character> operators = multiplicative.second;

            derivativeFormula = new StringBuilder(removeParentheses(components.get(0)));

            for (int i = 1; i < components.size(); i++) {
                String nextComp = components.get(i);

                String buffDeriv = symbolicDerivative(new FunctionVariable(derivativeFormula.toString())).getValue();
                String nextDeriv = symbolicDerivative(new FunctionVariable(nextComp)).getValue();

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
                derivativeFormula.append(compDeriv.getValue());
                derivativeFormula.append(operators.get(i));
            }
            derivativeFormula.append(symbolicDerivative(new FunctionVariable(components.get(components.size() - 1))).getValue());
            derivativeFormula.append(')');
        }

        if (negative) {
            derivativeFormula.insert(0, "(-(");
            derivativeFormula.append("))");
        }

        return new FunctionVariable(derivativeFormula.toString());
    }

    @Override
    public boolean verfiy(Variable<?>... args) {
        return args.length == 1 && args[0].getClass() == FunctionVariable.class;
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

    private static boolean isConstant(String formula) {
        try {
            double d = Double.parseDouble(formula);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean isPoly(String formula) {
        if (formula.length() < 5)
            return false;
        return formula.substring(0, 3).equals("x^(") && formula.charAt(formula.length() - 1) == ')' &&
                isConstant(formula.substring(3, formula.length() - 1));
    }

    private static String polyDeriv(String poly) {
        int i = 0;
        StringBuilder deriv = new StringBuilder();
        Double num = Double.parseDouble(poly.substring(3, poly.length() - 1));
        Double newPow = num - 1;
        return num.toString() + "*" + "x^(" + newPow + ")";
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
