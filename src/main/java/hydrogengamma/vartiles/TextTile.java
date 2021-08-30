package hydrogengamma.vartiles;

import hydrogengamma.model.variables.TextVariable;

public class TextTile extends DefaultTile {

    private final TextVariable text;

    public TextTile(TextVariable text, String label) {
        super(label);
        this.text = text;
    }

    @Override
    public String getContent() {
        return "$\\text{" + text.getValue() + "}$";
    }
}
