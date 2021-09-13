package hydrogengamma.controllers.vartiles;

import hydrogengamma.model.variables.NumericVariable;

public class NumericTile extends DefaultTile {

    private final NumericVariable number;

    public NumericTile(NumericVariable num, String label) {
        super(label);
        number = num;
    }

    @Override
    public String getContent() {
        return '$' + number.getValue().toString() + '$';
    }
}
