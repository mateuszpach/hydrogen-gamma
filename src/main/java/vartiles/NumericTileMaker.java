package vartiles;

import model.variables.NumericVariable;

public class NumericTileMaker implements TileMaker {

    private NumericVariable number;

    public NumericTileMaker(NumericVariable num) {
        number = num;
    }

    @Override
    public String makeHtml() {
        return null;
    }
}
