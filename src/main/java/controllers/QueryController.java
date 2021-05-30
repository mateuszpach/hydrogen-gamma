package controllers;

import model.TileMakersContainer;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import vartiles.TileMaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;

public class QueryController {

    public static class Tile {
        public String content;
        public String label;

        Tile(String content, String label) {
            this.content = content;
            this.label = label;
        }
    }

    private final Parser parser;
    private final ThymeleafTemplateEngine templateEngine;

    public QueryController(ThymeleafTemplateEngine templateEngine, Parser parser) {
        this.templateEngine = templateEngine;
        this.parser = parser;

        get("/query", (req, res) -> {
            String parameters = req.queryParams("params");
            String expression = req.queryParams("expr");
            System.out.println(parameters);
            System.out.println(expression);

            TileMakersContainer container = parser.parse(parameters, expression);

            ArrayList<Tile> tiles = new ArrayList<>();
            for (TileMaker tileMaker : container.getTileMakers()) {
                tiles.add(new Tile(tileMaker.getContent(), tileMaker.getLabel()));
            }

            Map<String, Object> model = new HashMap<>();
            model.put("tiles", tiles);
            return templateEngine.render(new ModelAndView(model, "query"));
        });
    }
}
