import model.TilesContainer;
import model.modules.NumberOperations;
import model.modules.TextAnalysis;
import model.variables.FunctionVariable;
import model.variables.MatrixVariable;
import model.variables.TextVariable;
import org.junit.jupiter.api.Test;
import model.variables.NumericVariable;
import vartiles.Tile;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class NumbersTest {
    Random rand = new Random();
    NumberOperations.NumberAdd add = new NumberOperations.NumberAdd();
    NumberOperations.NumberMulti mul = new NumberOperations.NumberMulti();
    TilesContainer container = new TilesContainer() {

        @Override
        public void addTile(Tile tile) {

        }

        @Override
        public ArrayList<Tile> getTiles() {
            return null;
        }
    };

    @Test
    void binaryAdd() {
        double aD = rand.nextDouble();
        NumericVariable aV = new NumericVariable(aD);
        double bD = rand.nextDouble();
        NumericVariable bV = new NumericVariable(bD);
        assertTrue(add.verify(aV, bV));
        assertTrue(mul.verify(aV, bV));
        assertEquals(add.execute(container, aV, bV).getValue(), aD + bD);
        assertEquals(mul.execute(container, aV, bV).getValue(), aD * bD);
    }

    @Test
    void emptyAdd() {
        //pretty useless, but it is a certain corner case
        assertTrue(add.verify());
        assertEquals(0d, add.execute(container).getValue());
    }

    @Test
    void chainAdd() {
        int testSize = 1500;
        double sum = 0;
        double multi = 1;
        NumericVariable[] args = new NumericVariable[testSize];
        for (int i = 0; i < testSize; ++i) {
            double a = rand.nextDouble();
            args[i] = new NumericVariable(a);
            sum += a;
        }
        //System.out.println(sum + " " + add.execute(container, args).getValue());
        NumericVariable[] args2 = new NumericVariable[15];
        for (int i = 0; i < 15; ++i) {
            double a = rand.nextDouble();
            args2[i] = new NumericVariable(a);
            multi *= a;
        }
        assertTrue(add.verify(args));
        assertTrue(mul.verify(args));
        assertTrue(mul.verify(args2));
        assertEquals(sum, add.execute(container, args).getValue());
        assertEquals(multi, mul.execute(container, args2).getValue());
    }

    @Test
    void onlyNumeric() {
        TextVariable t = new TextVariable("");
        MatrixVariable m = new MatrixVariable(new double[][]{{1.0, 1.0}});
        FunctionVariable f = new FunctionVariable("");
        NumericVariable n = new NumericVariable(1d);
        assertFalse(add.verify(t));
        assertFalse(add.verify(m));
        assertFalse(add.verify(f));
        assertTrue(add.verify(n));
        assertFalse(mul.verify(t));
        assertFalse(mul.verify(m));
        assertFalse(mul.verify(f));
        assertTrue(mul.verify(n));
    }


}

