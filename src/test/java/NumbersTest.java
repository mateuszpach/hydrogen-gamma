import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumbersTest {
    Random rand = new Random();
    AddNumbers add = new AddNumbers();

    @Test
    void binaryAdd() {
        double aD = rand.nextDouble();
        NumericVariable aV = new NumericVariable(aD);
        double bD = rand.nextDouble();
        NumericVariable bV = new NumericVariable(bD);
        NumericVariable sum = add.call(aV, bV);
        assertEquals(sum.value, aD + bD);
    }

    @Test
    void emptyAdd() {
        //pretty useless, but it is a certain corner case
        assertEquals(0d, add.call().value);
    }

    @Test
    void chainAdd() {
        int testSize = 1500;

        double sum = 0;
        NumericVariable[] args = new NumericVariable[testSize];
        for (int i = 0; i < testSize; ++i) {
            double a = rand.nextDouble();
            args[i] = new NumericVariable(a);
            sum += a;
        }
        System.out.println(sum + " " + add.call(args).value);
        assertEquals(sum, add.call(args).value);
    }


}
