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

    public State() {
        this.varBoxes = new TreeMap<>();
        this.futureVariables = new TreeMap<>();
        this.futureIndex = 0;
        this.container = new TilesContainerImpl();
    }

    public String getSubstitutionName() {
        return getSubstitutionName(futureIndex++);
    }

    public String getSubstitutionName(int index) {
        return "#" + index;
    }
}
