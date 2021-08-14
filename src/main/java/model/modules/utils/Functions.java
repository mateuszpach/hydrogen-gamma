package model.modules.utils;

import utils.Pair;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class Functions {

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

    public static Pair<ArrayList<String>, ArrayList<Character>> findSubcomponents(String formula, String searchedOpers) {
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

        components.add(formula.substring(prev));
        return new Pair<>(components, operations);
    }
}
