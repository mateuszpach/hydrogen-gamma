package model.modules;

import model.Module;
import model.TilesContainer;
import model.Variable;
import model.variables.TextVariable;

public class TextIdentity implements Module<TextVariable> {
    @Override
    public TextVariable execute(TilesContainer container, Variable<?>... args) {
        return (TextVariable) args[0];
    }

    @Override
    public boolean verify(Variable<?>... args) {
        if (args.length < 1)
            return false;
        try {
            TextVariable a = (TextVariable) args[0];
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
