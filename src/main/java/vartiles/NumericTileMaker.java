package vartiles;

import model.variables.NumericVariable;

public class NumericTileMaker extends DefaultTileMaker {

    private NumericVariable number;

    public NumericTileMaker(NumericVariable num) {
        number = num;
    }

    @Override
    public String getContent() {
        return '$' + number.getValue().toString() + '$'; // TODO: change to proper format
    }
}
