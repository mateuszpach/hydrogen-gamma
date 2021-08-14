package utils;

import java.util.TreeMap;
import java.util.TreeSet;

public class KnownFunctions {
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
}
