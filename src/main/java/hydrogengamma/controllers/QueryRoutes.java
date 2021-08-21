package hydrogengamma.controllers;

import hydrogengamma.model.ParserFactory;
import hydrogengamma.model.TilesContainer;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.apache.log4j.Logger;

import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.get;

public class QueryRoutes {

    private static final Logger logger = Logger.getLogger(QueryRoutes.class);

    public QueryRoutes(Javalin app) {
        app.routes(() -> get("/query", queryController));
    }

    private final Handler queryController = (ctx) -> {
        String parameters = Optional.ofNullable(ctx.req.getParameter("params")).orElse("");
        String expression = Optional.ofNullable(ctx.req.getParameter("expr")).orElse("");
        logger.info(String.format("Handling query with parameters: %s and expression: %s.", parameters, expression));

        Parser parser = ParserFactory.getParser();
        TilesContainer container = parser.parse(parameters, expression);

        ArrayList<TileData> tiles = new ArrayList<>();
        for (hydrogengamma.vartiles.Tile tile : container.getTiles()) {
            tiles.add(new TileData(tile.getContent(), tile.getLabel()));
        }
        Collections.reverse(tiles);

        Map<String, Object> model = new HashMap<>();
        model.put("params", parameters);
        model.put("expr", expression);
        model.put("tiles", tiles);

        ctx.render("/templates/query.html", model);
    };

    public static class TileData {
        public String content;
        public String label;

        TileData(String content, String label) {
            this.content = content;
            this.label = label;
        }
    }
}
