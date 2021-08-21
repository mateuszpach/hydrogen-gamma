package hydrogengamma.model.parsers.standard;

import hydrogengamma.controllers.Parser;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.TilesContainerImpl;
import hydrogengamma.model.modules.utils.ModuleException;
import hydrogengamma.vartiles.InfoTile;
import hydrogengamma.vartiles.Tile;
import org.apache.log4j.Logger;

public class StandardParser implements Parser {

    private static final Logger logger = Logger.getLogger(StandardParser.class);

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

        state.results.forEach((id, result) -> {  // TODO powinno być w Computer MICHAL
            if (id.charAt(0) != '0') { // TODO zrób czytelną funkcję na to MICHAL
                Tile tile = new InfoTile(result.value.getValue().toString(), result.text);
                state.container.addTile(tile);

                System.out.println("variable:" + id + " " + result.value.getValue());
            }
        });

        // TODO zaszłość, rzuć exception MICHAL
        state.msg = null;
        if (state.expressions.size() > 0) { // TODO usuń MICHAL
            try {
                computer.compute(state);
            } catch (ModuleException exception) {
                state.container = new TilesContainerImpl();
                state.container.addTile(new InfoTile(exception.toString(), "Module error"));
            }
            { // TODO wyodrębnij i loguj MATEUSZ
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
        return state.container;  // TODO wyodrębnij Container i nie modyfikuj state.container. MICHAL

        //TODO jeden duży try catch, różne catche dla różnych exception MICHAL
    }
}
