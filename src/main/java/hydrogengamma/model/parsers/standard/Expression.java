package hydrogengamma.model.parsers.standard;

import java.util.List;

public final class Expression {// final class can't be extended
    private final String id;
    private final String operationName;
    private final String label;
    private final List<String> subExpressions;

    public Expression(String id, String label, String operationName, List<String> subExpressions) {
        this.id = id;
        this.operationName = operationName;
        this.label = label;
        this.subExpressions = subExpressions;
    }

    @Override
    public String toString() {
        return this.getLabel() + " as : " + this.getId() + " = " + this.getOperationName() + "(" + String.join(", ", getSubExpressions()) + ")\n";
    }

    public String getId() {
        return id;
    }

    public String getOperationName() {
        return operationName;
    }

    public String getLabel() {
        return label;
    }

    public List<String> getSubExpressions() {
        return subExpressions;
    }
}
