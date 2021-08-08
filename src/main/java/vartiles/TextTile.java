package vartiles;

import model.variables.TextVariable;

public class TextTile extends DefaultTile {

    private TextVariable text;

    public TextTile(TextVariable txt) {
        text = txt;
    }

    @Override
    public String getContent() {
        return "$\\text{" + text.getValue() + "}$";
    }
}
