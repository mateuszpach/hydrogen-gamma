package model.parsers.standard;

public class ParsingException extends IllegalArgumentException {
    String msg;

    public ParsingException(String s) {
        super(s);
        msg = s;
    }
}