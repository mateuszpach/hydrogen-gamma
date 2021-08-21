package hydrogengamma.utils;

import hydrogengamma.model.modules.utils.Functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class FunctionLatex {

    public static Set<String> slashableFormulas = new TreeSet<>();

    static {
        slashableFormulas.add("sin(x)");
        slashableFormulas.add("cos(x)");
        slashableFormulas.add("ln(x)");
    }

    public static String latexForm(String formula) {

        System.out.println(formula);

        var formulas = Functions.findSubcomponents(formula, "+-");

        if (formulas.second.isEmpty()) {
            formulas = Functions.findSubcomponents(formula, "*/");
            if (formulas.second.isEmpty()) {
                formulas = Functions.findSubcomponents(formula, "^");
                if (formulas.second.isEmpty()) {
                    return slashableFormulas.contains(formula) ? "\\" + formula : formula;
                }
            }
        }

        List<String> latexForms = new ArrayList<>();
        for (String a : formulas.first) {
            latexForms.add(latexForm(a));
        }

        StringBuilder latexFormula = new StringBuilder(latexForms.get(0));
        for (int i = 0; i < formulas.second.size(); i++) {
            char op = formulas.second.get(i);
            if (op == '*')
                latexFormula.append(" \\dot ").append(latexForms.get(i + 1));
            else if (op == '/')
                latexFormula.insert(0, "\\frac{ ").append(" }{ ").append(latexForms.get(i + 1)).append(" }");
            else if (op == '^')
                latexFormula.append("^{ ").append(latexForms.get(i + 1)).append(" }");
            else
                latexFormula.append(' ').append(op).append(' ').append(latexForms.get(i + 1));
        }

        return latexFormula.toString();
    }

}
