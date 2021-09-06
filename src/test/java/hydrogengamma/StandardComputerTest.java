package hydrogengamma;

import hydrogengamma.controllers.Computer;
import hydrogengamma.controllers.Expression;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.TilesContainerImpl;
import hydrogengamma.model.Variable;
import hydrogengamma.model.parsers.standard.ParsingException;
import hydrogengamma.model.parsers.standard.StandardComputer;
import hydrogengamma.model.variables.NumericVariable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class StandardComputerTest {
    @Test
    void operationsThrowWhenVariableIsMissing() {
        Map<String, Variable<?>> variables = new TreeMap<>();
        ArrayList<Expression> operations = new ArrayList<>();
        variables.put("a", new NumericVariable(2));
        ArrayList<String> operation1 = new ArrayList<>();
        operation1.add("thisVariableWasNotGiven");
        operation1.add("a");
        operations.add(new Expression("b", "a+a", "+", operation1));
        Computer computer = new StandardComputer();
        assertThrows(ParsingException.class, () -> computer.compute(variables, operations));
    }

    @Test
    void operationsThrowWhenModuleIsMissing() {
        Map<String, Variable<?>> variables = new TreeMap<>();
        ArrayList<Expression> operations = new ArrayList<>();
        variables.put("a", new NumericVariable(2));
        ArrayList<String> operation1 = new ArrayList<>();
        operation1.add("a");
        operation1.add("a");
        operations.add(new Expression("b", "a+a", "++SuchModuleNameCouldNotExist++", operation1));
        Computer computer = new StandardComputer();
        assertThrows(ParsingException.class, () -> computer.compute(variables, operations));
    }

    @Test
    void operationsAreComputedCorrectly() {
        Map<String, Variable<?>> variables = new TreeMap<>();
        ArrayList<Expression> operations = new ArrayList<>();
        variables.put("a", new NumericVariable(2));
        ArrayList<String> operation1 = new ArrayList<>();
        operation1.add("a");
        operation1.add("a");
        ArrayList<String> operation2 = new ArrayList<>();
        operation2.add("a");
        operation2.add("b");
        ArrayList<String> operation3 = new ArrayList<>();
        operation3.add("b");
        operation3.add("c");
        operations.add(new Expression("b", "a+a", "+", operation1));
        operations.add(new Expression("c", "a+(a+a)", "+", operation2));
        operations.add(new Expression("d", "(a+a)+(a+(a+a))", "+", operation3));
        Computer computer = new StandardComputer();
        final TilesContainer[] tilesContainer = {new TilesContainerImpl()};
//        assertDoesNotThrow(() -> tilesContainer[0] = computer.compute(variables, operations));
//        assertEquals("$4.0$", tilesContainer[0].getTiles().get(0).getContent());
//        assertEquals("Sum of: a, a", tilesContainer[0].getTiles().get(0).getLabel());
//        assertEquals("$6.0$", tilesContainer[0].getTiles().get(1).getContent());
//        assertEquals("Sum of: a, a+a", tilesContainer[0].getTiles().get(1).getLabel());
//        assertEquals("$10.0$", tilesContainer[0].getTiles().get(2).getContent());
//        assertEquals("Sum of: a+a, a+(a+a)", tilesContainer[0].getTiles().get(2).getLabel()); TODO
    }

}
