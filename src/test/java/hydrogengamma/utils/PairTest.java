package hydrogengamma.utils;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PairTest {
    @Test
    void properValues() {
        Pair<String, Double> p1 = new Pair<>("Aa", 2.0);
        Pair<Integer, Pair<String, Double>> p2 = new Pair<>(8, p1);

        assertEquals("Aa", p1.first);
        assertEquals(2.0, p1.second);
        assertEquals(8, p2.first);
        assertEquals(p1, p2.second);
    }

    @Test
    void sameEqual() {
        Pair<String, Double> p1 = new Pair<>("Aa", 2.0);
        Pair<String, Double> p2 = new Pair<>("Aa", 2.0);

        assertEquals(p1, p2);
    }

    @Test
    void differentNotEqual() {
        Pair<String, Double> p1 = new Pair<>("Aa", 2.0);
        Pair<String, Double> p2 = new Pair<>("Aa", 1.0);

        assertNotEquals(p1, p2);
    }

    @Test
    void testHashCode() {
        Pair<String, Double> p1 = new Pair<>("Aa", 2.0);
        assertEquals(Objects.hash("Aa", 2.0), p1.hashCode());
    }
}
