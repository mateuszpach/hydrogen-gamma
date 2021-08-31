package hydrogengamma.model.parsers.standard;

import hydrogengamma.controllers.Loader;
import hydrogengamma.model.Extractors;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.FunctionVariable;
import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.variables.TextVariable;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class StandardLoader implements Loader {

    public Map<String, Variable<?>> load(String varDefinition) {
        //no # allowed, also remove whitespace
        varDefinition = varDefinition.replaceAll("#", "");
        System.out.println("var:" + varDefinition + ":");
        return this.parseVariables(varDefinition);
    }

    /* TODO:shall be removed (and some Michal's comment: so tree flattening is dumb and would be easily resolved by making identity modules not print anything)

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
                        // break here would parse "500" into 3 variables 5,0,0, while 'legit' check just makes sure a stranded '-' won't be tried to be parsed as a number

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
    */

    // note: varDefinition will contain whitespace, remove it from everywhere BUT textVariable content (don't leave it in variables names)
    private Map<String, Variable<?>> parseVariables(String varDefinition) {
        Map<String, Variable<?>> variableMap = new TreeMap<>();
        if (varDefinition.length() == 0)
            return variableMap;

        String[] variables = varDefinition.split(";");
        for (String var : variables) {
            String[] varNameAndDef = var.split("=");

            if (varNameAndDef.length != 2)
                throw new ParsingException("variable definition must contain exactly one '=' character: " + var);
            varNameAndDef[0] = varNameAndDef[0].replaceAll("\\s+", ""); // remove whitespace from name
            varNameAndDef[1] = varNameAndDef[1].strip();// strip leading and trailing whitespace from definition

            if (!varNameAndDef[0].matches("[a-zA-Z][\\w]*")) {
                throw new ParsingException("variable name must be alphanumeric not beginning with digit, but is:" + varNameAndDef[0]);
            }

            String varName = varNameAndDef[0];
            String varDef = varNameAndDef[1];

            for (Extractors extractorEnum : Extractors.values()) {
                if (extractorEnum.extractor.verify(varDef)) {
                    variableMap.put(varName, extractorEnum.extractor.extract(varDef));
                }
            }
        }
        return variableMap;
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
