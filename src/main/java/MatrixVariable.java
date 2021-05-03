import java.util.ArrayList;

public class MatrixVariable implements Variable {
    final ArrayList<ArrayList<Double>> value;

    MatrixVariable(ArrayList<ArrayList<Double>> value) {
        this.value = value;
    }
}
