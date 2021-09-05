package hydrogengamma;

import hydrogengamma.controllers.Expression;
import hydrogengamma.controllers.TreeBuilder;
import hydrogengamma.model.parsers.standard.StandardTreeBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StandardTreeBuilderTest {

    @Test
    void treeBuilderDoesNotThrowOnEmptyInput() {
        TreeBuilder treeBuilder = new StandardTreeBuilder();
        final String operation = "";
        final List<Expression>[] state = new ArrayList[]{new ArrayList<>()};
        assertDoesNotThrow(() -> state[0] = treeBuilder.build(operation));
        assertTrue(state[0].isEmpty());
    }

    @Test
    void prefixOperationsAreSimplifiedCorrectly() {
        TreeBuilder treeBuilder = new StandardTreeBuilder();
        final String operation = "+(+(+(a),a),a,+(a,+(a)))";
        final List<Expression>[] state = new ArrayList[]{new ArrayList<>()};
        assertDoesNotThrow(() -> state[0] = treeBuilder.build(operation));
        assertEquals(6, state[0].size());
        assertEquals("(a+a+a+a+a)", state[0].get(5).getLabel());
    }

    @Test
    void infixOperationsAreSimplifiedCorrectly() {
        TreeBuilder treeBuilder = new StandardTreeBuilder();
        final String operation = "a+a*(a+a*a)";
        final List<Expression>[] state = new ArrayList[]{new ArrayList<>()};
        assertDoesNotThrow(() -> state[0] = treeBuilder.build(operation));
        assertEquals(7, state[0].size());
        assertEquals("a*a", state[0].get(0).getLabel());
        assertEquals("a+a*a", state[0].get(1).getLabel());
        assertEquals("(a+a*a)", state[0].get(2).getLabel());
        assertEquals("a*(a+a*a)", state[0].get(3).getLabel());
        assertEquals("a+a*(a+a*a)", state[0].get(4).getLabel());
    }

    @Test
    void signsAreSimplifiedCorrectly() {
        TreeBuilder treeBuilder = new StandardTreeBuilder();
        final String operation = "*+++---+++-+--+-+-a";
        final List<Expression>[] state = new ArrayList[]{new ArrayList<>()};
        assertDoesNotThrow(() -> state[0] = treeBuilder.build(operation));
        assertEquals(2, state[0].size());
        assertEquals("*", state[0].get(0).getOperationName());
    }

    @Test
    void signsWithModulesAreSimplifiedCorrectly() {
        TreeBuilder treeBuilder = new StandardTreeBuilder();
        final String operation = "-add(a)";
        final List<Expression>[] state = new ArrayList[]{new ArrayList<>()};
        assertDoesNotThrow(() -> state[0] = treeBuilder.build(operation));
        assertEquals(4, state[0].size());
        assertEquals("add", state[0].get(0).getOperationName());
    }
}
