package model;

public abstract class Variable<T> {

    // TODO zrób value jako prywatne i ustal konstruktor Variable

    protected T value;

    public T getValue() {
        return value;
    }
}
