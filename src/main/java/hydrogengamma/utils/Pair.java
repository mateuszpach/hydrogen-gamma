package hydrogengamma.utils;

import java.util.Objects;

public class Pair<T, U> {

    public T first;
    public U second;

    public Pair(T t, U u) {
        first = t;
        second = u;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
