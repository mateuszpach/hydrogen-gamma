package model.parsers.standard;

import controllers.Parser;
import model.TilesContainer;
import model.TilesContainerImpl;
import model.modules.utils.ModuleException;
import vartiles.InfoTile;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

public class StandardParser implements Parser {

    private final Loader loader;
    private final Computer computer;

    public StandardParser(Loader loader, Computer computer) {
        this.loader = loader;
        this.computer = computer;
    }

    @Override
    public TilesContainer parse(String variables, String expression) { // runs load and compute session with error handling and tile building
        State state = new State(); //just so it can return immediately on empty input
        if (variables.equals("") && expression.equals(""))
            return state.container;
        state = loader.load(variables, expression);
        if (state.msg != null) {
            state.container = new TilesContainerImpl();
            state.container.addTile(new InfoTile(state.msg, "Parsing error"));
            return state.container;
        }
        Set<String> keys = state.varBoxes.keySet();
        String[] aKeys = keys.toArray(new String[keys.size()]);
        Collections.reverse(Arrays.asList(aKeys));
        for (String key : aKeys) {
            System.out.println("variable:" + key + " " + state.varBoxes.get(key).getValue());
            state.container.addTile(new InfoTile(state.varBoxes.get(key).getValue().toString(), key));
        }//after loading print variables
        state.msg = null;
        if (state.futureVariables.size() > 0) {
            try {
                computer.compute(state);
            } catch (ModuleException exception) {
                state.container = new TilesContainerImpl();
                state.container.addTile(new InfoTile(exception.toString(), "Module error"));
            }
            {
                for (String key : state.futureVariables.keySet()) {
                    System.out.print("future: " + key + " = " + state.futureVariables.get(key).first + " ( ");
                    for (String name : state.futureVariables.get(key).second) {
                        System.out.print(name + " ");
                    }
                    System.out.println(")");
                }
            }
        }
        if (state.msg != null) {
            state.container = new TilesContainerImpl();
            state.container.addTile(new InfoTile(state.msg, "Computing error"));
            return state.container;
        }
        return state.container;

    }
}
