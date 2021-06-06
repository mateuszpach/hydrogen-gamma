package vartiles;

import model.variables.NumericVariable;

public class NumericTile extends DefaultTile {

    private NumericVariable number;

    public NumericTile(NumericVariable num) {
        number = num;
    }

    @Override
    public String getContent() {
        return '$' + number.getValue().toString() + '$'; // TODO: change to proper format
    }
}
