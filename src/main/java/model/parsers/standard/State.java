package model.parsers.standard;

import model.TilesContainer;
import model.TilesContainerImpl;
import model.Variable;
import utils.Pair;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class State {
    public Map<String, Variable<?>> varBoxes;
    public TilesContainer container;
    public Map<String, Pair<String, ArrayList<String>>> futureVariables;
    int futureIndex;
    public String msg;

    public State() {
        this.varBoxes = new TreeMap<>();
        this.futureVariables = new TreeMap<>();
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
}
