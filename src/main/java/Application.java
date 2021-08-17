import controllers.QueryRoutes;
import io.javalin.Javalin;
import org.apache.log4j.PropertyConfigurator;


public class Application {
    public static void main(String[] args) {
        String log4jConfPath = "src/main/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        Javalin app = Javalin.create(config -> config.addStaticFiles("/static")).start(7000);
        new QueryRoutes(app);
    }
}
