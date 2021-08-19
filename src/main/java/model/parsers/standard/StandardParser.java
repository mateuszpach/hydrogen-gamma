package model.parsers.standard;

import controllers.Parser;
import model.TilesContainer;
import model.TilesContainerImpl;
import model.modules.utils.ModuleException;
import vartiles.InfoTile;
import vartiles.Tile;

public class StandardParser implements Parser {

    private final Loader loader;
    private final Computer computer;

    public StandardParser(Loader loader, Computer computer) {
        this.loader = loader;
        this.computer = computer;
    }

    @Override
    public TilesContainer parse(String variables, String expression) { // runs load and compute session with error handling and tile building
        if (variables.equals("") && expression.equals("")) {
            //just so it can return immediately on empty input
            State state = new State();
            return state.container;
        }

        State state = loader.load(variables, expression);
        if (state.msg != null) {
            state.container = new TilesContainerImpl();
            state.container.addTile(new InfoTile(state.msg, "Parsing error"));
            return state.container;
        }

        state.results.forEach((id, result) -> {
            if (id.charAt(0) != '0') {
                Tile tile = new InfoTile(result.value.getValue().toString(), result.text);
                state.container.addTile(tile);

                System.out.println("variable:" + id + " " + result.value.getValue());
            }
        });


        state.msg = null;
        if (state.expressions.size() > 0) {
            try {
                computer.compute(state);
            } catch (ModuleException exception) {
                state.container = new TilesContainerImpl();
                state.container.addTile(new InfoTile(exception.toString(), "Module error"));
            }
            {
                for (String key : state.expressions.keySet()) {
                    System.out.print("future: " + key + " = " + state.expressions.get(key).functionName + " ( ");
                    for (String name : state.expressions.get(key).subexpressionsIds) {
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
