package hydrogengamma.model.parsers.standard;

import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.TilesContainerImpl;
import hydrogengamma.model.Variable;
import hydrogengamma.utils.Pair;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class State {
    //public TilesContainer container;
    public Map<String, Expression> expressions;// TODO: make it private, with some kind of lock that will prohibit modification once it leaves loader (some proxy) MICHAL
    private int futureIndex;

    public State() {
        this.expressions = new TreeMap<>();
        this.futureIndex = 0;
    }

    public void addExpression(String name, Expression exp) {
        if (expressions.containsKey(name))
            throw new ParsingException(String.format("attempted redefinition of variable %s\n", name));
        expressions.put(name, exp);
    }

    public boolean containsKey(String x) {
        return expressions.containsKey(x);
    }

    public ArrayList<String> getComputationOrder() {
        ArrayList<String> order = new ArrayList<>();
        if (futureIndex == 0)
            return order;
        String top = getSubstitutionName(futureIndex - 1);
        traverse(top, order);
        System.out.println("Computation order: " + order);
        return order;
    }

    private void traverse(String node, ArrayList<String> order) {
        if (expressions.get(node).ready) {
            return;
        }
        for (String component : expressions.get(node).subexpressionsIds) {
            traverse(component, order);
        }
        order.add(node);
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

    public boolean isUserMadeName(String x) {
        return x.matches("[a-zA-Z]]+");
    }

    public static class Expression {
        String functionName;
        ArrayList<String> subexpressionsIds;
        String text;
        boolean ready;
        private Variable<?> value;

        public Expression(String functionName, ArrayList<String> subexpressionsIds) {
            this.functionName = functionName;
            this.subexpressionsIds = subexpressionsIds;
            this.ready = false;
        }

        public Expression(String text, Variable<?> value) {
            this.text = text;
            this.value = value;
            this.ready = true;
        }

        public void setVariable(String text, Variable<?> value) {
            if (this.ready)
                throw new ParsingException(String.format("An attempt to reassign value of %s,%s(%s) was made, was %s\n", this.text, this.functionName, this.subexpressionsIds, this.value));
            this.text = text;
            this.value = value;
            this.ready = true;
        }

        public Variable<?> getVariable() {
            if (!this.ready)
                throw new ParsingException(String.format("An attempt to obtain value of %s(%s) was made before it was assigned\n", this.functionName, this.subexpressionsIds));
            return this.value;
        }

        @Override
        public String toString() {
            if (this.ready)
                return getVariable().getValue().toString();
            else
                return String.format("%s(%s)", functionName, String.join(", ", this.subexpressionsIds));
        }
    }

}
