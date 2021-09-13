package hydrogengamma.model.variables;

import hydrogengamma.model.parsers.standard.Variable;

public class VoidVariable implements Variable<Void> {
    @Override
    public Void getValue() {
        return null;
    }
}
