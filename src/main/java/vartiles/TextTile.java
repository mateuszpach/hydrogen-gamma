package vartiles;

import model.variables.TextVariable;

public class TextTile extends DefaultTile {

    private TextVariable text;

    public TextTile(TextVariable text) {
        this.text = text;
    }

    @Override
    public String getContent() {
        return "$\\text{" + text.getValue() + "}$";
    }
}
