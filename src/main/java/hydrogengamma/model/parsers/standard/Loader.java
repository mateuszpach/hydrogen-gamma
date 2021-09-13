package hydrogengamma.model.parsers.standard;

import java.util.Map;

public interface Loader {
    Map<String, Variable<?>> load(String variables);
}
