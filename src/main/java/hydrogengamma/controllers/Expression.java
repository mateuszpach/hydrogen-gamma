package hydrogengamma.controllers;

import java.util.List;

public final class Expression {// final can't be extended
    // I'm not so sure about this class, but it's a final container (and otherwise there is problem defining what is returned by treeBuilder)
    final public String id;
    final public String operationName;
    final public String label;
    final public List<String> subExpressions;

    public Expression(String id, String label, String operationName, List<String> subExpressions) {
        this.id = id;
        this.operationName = operationName;
        this.label = label;
        this.subExpressions = subExpressions;
    }

    @Override
    public String toString() {
        return this.label + " as : " + this.id + " = " + this.operationName + "(" + String.join(", ", subExpressions) + ")\n";
    }
}
