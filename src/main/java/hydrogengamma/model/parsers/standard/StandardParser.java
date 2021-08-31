package hydrogengamma.model.parsers.standard;

import hydrogengamma.controllers.Computer;
import hydrogengamma.controllers.Loader;
import hydrogengamma.controllers.Parser;
import hydrogengamma.controllers.TreeBuilder;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.TilesContainerImpl;
import hydrogengamma.model.modules.utils.ModuleException;
import hydrogengamma.vartiles.InfoTile;
import org.apache.log4j.Logger;

public class StandardParser implements Parser {

    private static final Logger logger = Logger.getLogger(StandardParser.class);

    private final Loader loader;
    private final Computer computer;
    private final TreeBuilder treeBuilder;

    public StandardParser(Loader loader, TreeBuilder treeBuilder, Computer computer) {
        this.loader = loader;
        this.computer = computer;
        this.treeBuilder = treeBuilder;
    }

    @Override
    public TilesContainer parse(String variables, String operation) { // runs load and compute session with error handling and tile building
        if (variables.equals("") && operation.equals("")) {
            //just so it can return immediately on empty input
            return new TilesContainerImpl();
        }

        /* //TODO: redo or remove MICHAL
        State state;
        { // TODO wyodrębnij i loguj MATEUSZ
            for (String key : state.expressions.keySet()) {
                if (state.expressions.get(key).ready) {
                    System.out.println(key + " : " + state.expressions.get(key).text + " = " + state.expressions.get(key).getVariable().getValue().toString());
                } else {
                    System.out.print("future: " + key + " = " + state.expressions.get(key).functionName + " ( ");
                    for (String name : state.expressions.get(key).subexpressionsIds) {
                        System.out.print(name + " ");
                    }
                    System.out.println(")");
                }
            }
        }
        */
        TilesContainer container;
        try {
            container = computer.compute(loader.load(variables), treeBuilder.build(operation));
        } catch (ParsingException e) {
            return new TilesContainerImpl(new InfoTile(e.msg, "Computing error"));
        } catch (ModuleException exception) {
            return new TilesContainerImpl(new InfoTile(exception.toString(), "Module error"));
        }
        return container;

    }
}
