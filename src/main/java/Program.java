import controllers.Parser;
import controllers.QueryController;
import model.ParserImplDummy;
import org.apache.log4j.PropertyConfigurator;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import static spark.Spark.staticFiles;

class Program {
    public static void main(String[] args) {
        String log4jConfPath = "src/main/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);

        staticFiles.location("/static");

        Parser parser = new ParserImplDummy();
        ThymeleafTemplateEngine templateEngine = new ThymeleafTemplateEngine();
        QueryController queryController = new QueryController(templateEngine, parser);
    }
}
