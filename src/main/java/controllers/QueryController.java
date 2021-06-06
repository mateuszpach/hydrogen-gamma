package controllers;

import model.TilesContainer;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

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
            String regex = "\\$"; // TODO: check if $ is only character/phrase that affects HTML and should be filtered out
            if (parameters == null)// not taking care of your own nulls is kinda mean
                parameters = "";
            else
                parameters = parameters.replaceAll(regex, "");
            if (expression == null)
                expression = "";
            else
                expression = expression.replaceAll(regex, "");

            TilesContainer container = parser.parse(parameters, expression);

            ArrayList<Tile> tiles = new ArrayList<>();
            for (vartiles.Tile tile : container.getTiles()) {
                tiles.add(new Tile(tile.getContent(), tile.getLabel()));
            }

            Map<String, Object> model = new HashMap<>();
            model.put("tiles", tiles);
            return templateEngine.render(new ModelAndView(model, "query"));
        });
    }
}
