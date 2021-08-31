package hydrogengamma;

import hydrogengamma.controllers.Computer;
import hydrogengamma.controllers.Expression;
import hydrogengamma.controllers.Loader;
import hydrogengamma.controllers.TreeBuilder;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.TilesContainerImpl;
import hydrogengamma.model.Variable;
import hydrogengamma.model.parsers.standard.ParsingException;
import hydrogengamma.model.parsers.standard.StandardComputer;
import hydrogengamma.model.parsers.standard.StandardLoader;
import hydrogengamma.model.parsers.standard.StandardTreeBuilder;
import hydrogengamma.model.variables.MatrixVariable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class StandardParserTest {
    Loader loader = new StandardLoader();
    Computer computer = new StandardComputer();
    TreeBuilder treeBuilder = new StandardTreeBuilder();

    @Test
    void removeWhitespaceAndHashtags() {
        Map<String, Variable<?>> state = loader.load("#a#a#=#1#;b    b   =   1   ;\nc\nc\n=\n1\n;\rd\r\rd\r=\r1\r; e e = 1 ");
        assertTrue(state.containsKey("aa"));
        assertTrue(state.containsKey("bb"));
        assertTrue(state.containsKey("cc"));
        assertTrue(state.containsKey("dd"));
        assertTrue(state.containsKey("ee"));
        assertEquals(state.size(), 5);
    }

    @Test
    void parseVariableDefinitionStructure() {
        //returns message on throw (to parser to print status for user)
        assertThrows(ParsingException.class, () -> loader.load("a;1"));
        assertThrows(ParsingException.class, () -> loader.load("0a=1"));
        assertThrows(ParsingException.class, () -> loader.load("a=\""));
        assertThrows(ParsingException.class, () -> loader.load("a=("));
        assertThrows(ParsingException.class, () -> loader.load("a=["));
        assertThrows(ParsingException.class, () -> loader.load("a=aaa"));
        assertThrows(ParsingException.class, () -> loader.load("a=1..1"));
        assertThrows(ParsingException.class, () -> loader.load("a=--1.1"));
        assertThrows(ParsingException.class, () -> loader.load("a=[1,1/1]"));
        assertThrows(ParsingException.class, () -> loader.load("a=[1..1]"));
        assertThrows(ParsingException.class, () -> loader.load("a=[--1.1]"));
        assertThrows(ParsingException.class, () -> loader.load("a=[]"));
        assertThrows(ParsingException.class, () -> loader.load("a=[/]"));
    }

    @Test
    void correctVariablesPass() {
        final Map<String, Variable<?>>[] state = new Map[]{new TreeMap<>()};//cause lambda
        assertDoesNotThrow(() -> state[0] = loader.load("a=1;b=-1;c=1.1;d=-1.1;e=\"\";f=\"\"\";g=(1);h=(1,-1,1.1,-1.1);i=[1];j=[1/-1/1.1/-1.1];k=[1,-1/1.1,-1.1];l=[1,-1,1.1,-1.1]"));
        assertEquals(state[0].size(), 12);
        assertEquals(1d, state[0].get("a").getValue());
        assertEquals(-1d, state[0].get("b").getValue());
        assertEquals(1.1d, state[0].get("c").getValue());
        assertEquals(-1.1d, state[0].get("d").getValue());
        assertEquals((""), state[0].get("e").getValue());
        assertEquals(("\""), state[0].get("f").getValue());
        assertEquals(("(1)"), state[0].get("g").getValue());
        assertEquals(("(1,-1,1.1,-1.1)"), state[0].get("h").getValue());
        assertEquals(new MatrixVariable(new double[][]{{1}}), state[0].get("i"));
        assertEquals(new MatrixVariable(new double[][]{{1}, {-1}, {1.1}, {-1.1}}), state[0].get("j"));
        assertEquals(new MatrixVariable(new double[][]{{1, -1}, {1.1, -1.1}}), state[0].get("k"));
        assertEquals(new MatrixVariable(new double[][]{{1, -1, 1.1, -1.1}}), state[0].get("l"));
    }
    @Test
    void operationsAreSimplifiedCorrectly() {//input is changed into list of hydrogengamma.model.variables in postorder with recipes
        final List<Expression>[] state = new ArrayList[]{new ArrayList<>()};
        assertDoesNotThrow(() -> state[0] = treeBuilder.build("+(+(+(a),a),a,+(a,+(a)))"));
        assertEquals(6, state[0].size());
        assertEquals("(a+a+a+a+a)", state[0].get(5).label);
        System.out.println(state[0]);
        assertDoesNotThrow(() -> state[0] = treeBuilder.build("*+++---+++-+--+-+-a"));
        assertEquals(2, state[0].size());
        assertEquals("*", state[0].get(0).operationName);

        assertDoesNotThrow(() -> state[0] = treeBuilder.build("a+a*(a+a*a)"));
        System.out.println(state[0]);
        assertEquals(7, state[0].size());
        assertEquals("a*a", state[0].get(0).label);
        assertEquals("a+a*a", state[0].get(1).label);
        assertEquals("(a+a*a)", state[0].get(2).label);
        assertEquals("a*(a+a*a)", state[0].get(3).label);
        assertEquals("a+a*(a+a*a)", state[0].get(4).label);
    }

    @Test
    void operationsAreComputedCorrectly() {
        final TilesContainer[] tilesContainer = {new TilesContainerImpl()};
        assertDoesNotThrow(() -> tilesContainer[0] = computer.compute(
                loader.load("a=2"),
                treeBuilder.build("+(+(+(a),a),a,+(a,+(a)))")));
        assertEquals("$10.0$", tilesContainer[0].getTiles().get(5).getContent());

        assertDoesNotThrow(() -> tilesContainer[0] = computer.compute(
                loader.load("a=2"),
                treeBuilder.build("*+++---+++-+--+-+-a")));
        assertEquals("$2.0$", tilesContainer[0].getTiles().get(1).getContent());

        assertDoesNotThrow(() -> tilesContainer[0] = computer.compute(
                loader.load("a=2"),
                treeBuilder.build("a+a*(a+a*a)")));
        assertEquals("$14.0$", tilesContainer[0].getTiles().get(4).getContent());
    }
}
