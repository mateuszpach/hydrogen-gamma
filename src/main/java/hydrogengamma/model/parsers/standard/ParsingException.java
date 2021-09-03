package hydrogengamma.model.parsers.standard;

public class ParsingException extends IllegalArgumentException {
    private final String msg;

    public ParsingException(String s) {
        super(s);
        msg = s;
    }

    @Override
    public String toString() {
        return msg;
    }
}