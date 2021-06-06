import model.modules.NumberAdder;
import model.TilesContainer;
import org.junit.jupiter.api.Test;
import model.variables.NumericVariable;
import vartiles.Tile;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumbersTest {
    Random rand = new Random();
    NumberAdder add = new NumberAdder();

    @Test
    void binaryAdd() {
        TilesContainer container = new TilesContainer() {

            @Override
            public void addTile(Tile tile) {

            }

            @Override
            public ArrayList<Tile> getTiles() {
                return null;
            }
        };
        double aD = rand.nextDouble();
        NumericVariable aV = new NumericVariable(aD);
        double bD = rand.nextDouble();
        NumericVariable bV = new NumericVariable(bD);
        NumericVariable sum = add.execute(container, aV, bV);
        assertEquals(sum.getValue(), aD + bD);
    }

    @Test
    void emptyAdd() {
        //pretty useless, but it is a certain corner case
        TilesContainer container = new TilesContainer() {

            @Override
            public void addTile(Tile tile) {

            }

            @Override
            public ArrayList<Tile> getTiles() {
                return null;
            }
        };
        assertEquals(0d, add.execute(container).getValue());
    }

    @Test
    void chainAdd() {
        int testSize = 1500;
        TilesContainer container = new TilesContainer() {

            @Override
            public void addTile(Tile tile) {

            }

            @Override
            public ArrayList<Tile> getTiles() {
                return null;
            }
        };

        double sum = 0;
        NumericVariable[] args = new NumericVariable[testSize];
        for (int i = 0; i < testSize; ++i) {
            double a = rand.nextDouble();
            args[i] = new NumericVariable(a);
            sum += a;
        }
        System.out.println(sum + " " + add.execute(container, args).getValue());
        assertEquals(sum, add.execute(container, args).getValue());
    }


}

