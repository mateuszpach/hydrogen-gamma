package model;

import utils.Pair;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ParserImplState {
    public Map<String, Variable<?>> varBoxes;
    public TilesContainer container;
    public Map<String, Pair<String, ArrayList<String>>> futureVariables;
    int futureIndex;

    public ParserImplState() {
        this.varBoxes = new TreeMap<>();
        this.futureVariables = new TreeMap<>();
        this.futureIndex = 0;
        this.container = new TilesContainerImpl();

    }
}
