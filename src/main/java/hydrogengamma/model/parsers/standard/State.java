package hydrogengamma.model.parsers.standard;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.TilesContainerImpl;
import hydrogengamma.model.Variable;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class State {
    public TilesContainer container;
    public Map<String, Expression> expressions;
    public Map<String, Result> results;
    int futureIndex;
    public String msg;

    public State() {
        this.expressions = new TreeMap<>();
        this.results = new TreeMap<>();
        this.futureIndex = 0;
        this.container = new TilesContainerImpl();
        this.msg = null;
    }

    public String getSubstitutionName() {
        return getSubstitutionName(futureIndex++);
    }

    public String getSubstitutionName(int index) {
        return index + "var";
    }

    public String constantName(Double x) {
        return "0" + x.toString().replaceAll("-", "m").replaceAll("\\.", "d");
    }

    public static class Expression {
        String functionName;
        ArrayList<String> subexpressionsIds;

        public Expression(String functionName, ArrayList<String> subexpressionsIds) {
            this.functionName = functionName;
            this.subexpressionsIds = subexpressionsIds;
        }
    }

    public static class Result {
        String text;
        Variable<?> value;

        public Result(String text, Variable<?> value) {
            this.text = text;
            this.value = value;
        }
    }
}
