package hydrogengamma.model.parsers.standard;

import hydrogengamma.model.Variable;

import java.util.Map;

public interface Loader {
    Map<String, Variable<?>> load(String variables);
}
