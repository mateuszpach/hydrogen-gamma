package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.TextVariable;

public class TextIdentity implements Module<TextVariable> {
    @Override
    public TextVariable execute(TilesContainer container, Variable<?>... args) {
        return (TextVariable) args[0];
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 1 && args[0] instanceof TextVariable;
    }
}
