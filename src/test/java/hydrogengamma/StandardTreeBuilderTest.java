package hydrogengamma;

import hydrogengamma.model.parsers.standard.Expression;
import hydrogengamma.model.parsers.standard.ParsingException;
import hydrogengamma.model.parsers.standard.TreeBuilder;
import hydrogengamma.model.parsers.standard.treebuilders.StandardTreeBuilder;
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
    void trowsOnRandomComma() {
        TreeBuilder treeBuilder = new StandardTreeBuilder();
        final String operation = "a,a";
        assertThrows(ParsingException.class, () -> treeBuilder.build(operation));
    }

    @Test
    void trowsOnMismatchedParenthesis() {
        TreeBuilder treeBuilder = new StandardTreeBuilder();
        assertThrows(ParsingException.class, () -> treeBuilder.build("(a+a"));
        assertThrows(ParsingException.class, () -> treeBuilder.build("a+a)"));
    }

    @Test
    void prefixOperationsAreSimplifiedCorrectly() {
        TreeBuilder treeBuilder = new StandardTreeBuilder();
        final String operation = "function(function(a),a,function(a,a))";
        final List<Expression>[] state = new ArrayList[]{new ArrayList<>()};
        assertDoesNotThrow(() -> state[0] = treeBuilder.build(operation));
        assertEquals(3, state[0].size());
        assertEquals("function(a)", state[0].get(0).getLabel());
        assertEquals("function(a,a)", state[0].get(1).getLabel());
        assertEquals("function(function(a),a,function(a,a))", state[0].get(2).getLabel());
    }

    @Test
    void infixOperationsAreSimplifiedCorrectly() {
        TreeBuilder treeBuilder = new StandardTreeBuilder();
        final String operation = "(a+a)+a*(a+a*a)+a";
        final List<Expression>[] state = new ArrayList[]{new ArrayList<>()};
        assertDoesNotThrow(() -> state[0] = treeBuilder.build(operation));
        System.out.println(state[0]);
        assertEquals(8, state[0].size());
        assertEquals("a+a", state[0].get(0).getLabel());
        assertEquals("(a+a)", state[0].get(1).getLabel());
        assertEquals("a*a", state[0].get(2).getLabel());
        assertEquals("a+a*a", state[0].get(3).getLabel());
        assertEquals("(a+a*a)", state[0].get(4).getLabel());
        assertEquals("a*(a+a*a)", state[0].get(5).getLabel());
        assertEquals("(a+a)+a*(a+a*a)", state[0].get(6).getLabel());
        assertEquals("(a+a)+a*(a+a*a)+a", state[0].get(7).getLabel());
    }

    @Test
    void signIsNotGluedToFunctionName() {
        TreeBuilder treeBuilder = new StandardTreeBuilder();
        final String operation = "(a)*sin(a*a+a*a)";
        final List<Expression>[] state = new ArrayList[]{new ArrayList<>()};
        assertDoesNotThrow(() -> state[0] = treeBuilder.build(operation));
        assertEquals(6, state[0].size());
        assertEquals("(a)", state[0].get(0).getLabel());
        assertEquals("a*a", state[0].get(1).getLabel());
        assertEquals("a*a", state[0].get(2).getLabel());
        assertEquals("a*a+a*a", state[0].get(3).getLabel());
        assertEquals("sin(a*a+a*a)", state[0].get(4).getLabel());
        assertEquals("(a)*sin(a*a+a*a)", state[0].get(5).getLabel());
    }

    @Test
    void cornerCases() {
        TreeBuilder treeBuilder = new StandardTreeBuilder();
        final String operation = "function(a,a+a,a)";
        final List<Expression>[] state = new ArrayList[]{new ArrayList<>()};
        assertDoesNotThrow(() -> state[0] = treeBuilder.build(operation));
        assertEquals(2, state[0].size());
        assertEquals("a+a", state[0].get(0).getLabel());
        assertEquals("function(a,a+a,a)", state[0].get(1).getLabel());
    }

    @Test
    void signsAreSimplifiedCorrectly() {
        TreeBuilder treeBuilder = new StandardTreeBuilder();
        final String operation = "*+++---+++-+--+-+-a";
        final List<Expression>[] state = new ArrayList[]{new ArrayList<>()};
        assertDoesNotThrow(() -> state[0] = treeBuilder.build(operation));
        assertEquals(1, state[0].size());
        assertEquals("*", state[0].get(0).getOperationName());
    }

    @Test
    void signsWithModulesAreSimplifiedCorrectly() {
        TreeBuilder treeBuilder = new StandardTreeBuilder();
        final String operation = "-sin(a)";
        final List<Expression>[] state = new ArrayList[]{new ArrayList<>()};
        assertDoesNotThrow(() -> state[0] = treeBuilder.build(operation));
        assertEquals(2, state[0].size());
        assertEquals("sin(a)", state[0].get(0).getLabel());
        assertEquals("-sin(a)", state[0].get(1).getLabel());
        assertEquals("-", state[0].get(1).getOperationName());
    }

}
