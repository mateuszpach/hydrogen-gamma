package hydrogengamma.model.parsers.standard;

import hydrogengamma.model.Extractors;
import hydrogengamma.model.Variable;

import java.util.Map;
import java.util.TreeMap;

public class StandardLoader implements Loader {

    public Map<String, Variable<?>> load(String varDefinition) {
        varDefinition = varDefinition.replaceAll("#", "");
        System.out.println("var:" + varDefinition + ":");
        return this.parseVariables(varDefinition);
    }


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

            boolean found = false;
            for (Extractors extractorEnum : Extractors.values()) {
                if (extractorEnum.extractor.verify(varDef)) {
                    variableMap.put(varName, extractorEnum.extractor.extract(varDef));
                    found = true;
                    break;
                }
            }
            if (!found)
                throw new ParsingException("Can't parse provided definition: " + varDefinition + " to any variable type.");
        }
        return variableMap;
    }
}
