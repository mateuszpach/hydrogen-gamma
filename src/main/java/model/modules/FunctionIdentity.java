package model.modules;

import model.Module;
import model.TilesContainer;
import model.Variable;
import model.variables.FunctionVariable;
import model.variables.MatrixVariable;
import model.variables.NumericVariable;
import model.variables.TextVariable;

import javax.xml.validation.Validator;

public class FunctionIdentity implements Module<FunctionVariable> {
    @Override
    public FunctionVariable execute(TilesContainer container, Variable<?>... args) {
        return (FunctionVariable) args[0];
    }

    @Override
    public boolean verify(Variable<?>... args) {
        if (args.length < 1)
            return false;
        try {
            FunctionVariable a = (FunctionVariable) args[0];
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
