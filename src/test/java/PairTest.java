import org.junit.jupiter.api.Test;
import util.Pair;

import static org.junit.jupiter.api.Assertions.*;

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
}
