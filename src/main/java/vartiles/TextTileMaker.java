package vartiles;

import model.variables.TextVariable;

public class TextTileMaker implements TileMaker {

    private TextVariable text;

    public TextTileMaker(TextVariable txt) {
        text = txt;
    }

    @Override
    public String makeHtml() {
        return null;
    }
}
