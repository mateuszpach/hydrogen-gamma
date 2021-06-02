package vartiles;

import model.variables.TextVariable;

public class TextTileMaker extends DefaultTileMaker {

    private TextVariable text;

    public TextTileMaker(TextVariable txt) {
        text = txt;
    }

    @Override
    public String getContent() {
        return '$' + text.getValue() + '$'; // TODO: change to proper format
    }
}
