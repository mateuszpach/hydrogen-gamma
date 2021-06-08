package model.modules.utils;

import utils.Pair;

import java.util.ArrayList;

public interface Functions {


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

        components.add(formula.substring(prev, formula.length()));
        return new Pair<>(components, operations);
    }
}
