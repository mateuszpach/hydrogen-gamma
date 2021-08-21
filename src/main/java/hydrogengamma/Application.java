package hydrogengamma;

import hydrogengamma.controllers.QueryRoutes;
import io.javalin.Javalin;
import org.apache.log4j.PropertyConfigurator;

import static io.javalin.apibuilder.ApiBuilder.get;


public class Application {
    public static void main(String[] args) {
        String log4jConfPath = "src/main/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        Javalin app = Javalin.create(config -> config.addStaticFiles("/static")).start(7000);

        app.routes(() -> get("/", ctx -> ctx.redirect("/query")));
        new QueryRoutes(app);
    }
}
