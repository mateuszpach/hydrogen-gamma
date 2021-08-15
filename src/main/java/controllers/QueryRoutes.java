package controllers;

import io.javalin.Javalin;
import io.javalin.http.Handler;
import model.ParserImpl;
import model.TilesContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.get;

public class QueryRoutes {

    public static class Tile {
        public String content;
        public String label;

        Tile(String content, String label) {
            this.content = content;
            this.label = label;
        }
    }

    public QueryRoutes(Javalin app) {
        app.routes(() -> {
            get("/query", queryController);
        });
    }

    private final Handler queryController = (ctx) -> {
        String parameters = ctx.req.getParameter("params");
        String expression = ctx.req.getParameter("expr");
        System.out.println(parameters);
        System.out.println(expression);
        String regex = "\\$"; // TODO: check if $ is only character/phrase that affects HTML and should be filtered out
        if (parameters == null)// not taking care of your own nulls is kinda mean
            parameters = "";
        else
            parameters = parameters.replaceAll(regex, "");
        if (expression == null)
            expression = "";
        else
            expression = expression.replaceAll(regex, "");

        Parser parser = new ParserImpl();
        TilesContainer container = parser.parse(parameters, expression);

        ArrayList<Tile> tiles = new ArrayList<>();
        for (vartiles.Tile tile : container.getTiles()) {
            tiles.add(new Tile(tile.getContent(), tile.getLabel()));
        }
        Collections.reverse(tiles);// why?

        Map<String, Object> model = new HashMap<>();
        model.put("tiles", tiles);

        ctx.render("/templates/query.html", model);
    };
}
