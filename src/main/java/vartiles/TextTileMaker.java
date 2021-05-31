package vartiles;

import model.variables.TextVariable;

public class TextTileMaker extends DefaultTileMaker {

    private TextVariable text;

    public TextTileMaker(TextVariable txt) {
        text = txt;
    }

    @Override
    public String getContent() {
        return '$' + text.value + '$'; // TODO: change to proper format
    }
}
