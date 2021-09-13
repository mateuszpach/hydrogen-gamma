package hydrogengamma;

import hydrogengamma.model.parsers.standard.Loader;
import hydrogengamma.model.parsers.standard.ParsingException;
import hydrogengamma.model.parsers.standard.Variable;
import hydrogengamma.model.parsers.standard.loaders.StandardLoader;
import hydrogengamma.model.variables.MatrixVariable;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class StandardLoaderTest {

    @Test
    void loaderDoesNotThrowOnEmptyInput() {
        Loader loader = new StandardLoader();
        final String variables = "";
        final Map[] state = new Map[]{new TreeMap<>()};//cause lambda
        assertDoesNotThrow(() -> state[0] = loader.load(variables));
        assertTrue(state[0].isEmpty());
    }

    @Test
    void removeWhitespaceAndHashtags() {
        Loader loader = new StandardLoader();
        final String variables = "#a#a#=#1#;b    b   =   1   ;\nc\nc\n=\n1\n;\rd\r\rd\r=\r1\r; e e = 1 ";
        Map<String, Variable<?>> state = loader.load(variables);
        assertTrue(state.containsKey("aa"));
        assertTrue(state.containsKey("bb"));
        assertTrue(state.containsKey("cc"));
        assertTrue(state.containsKey("dd"));
        assertTrue(state.containsKey("ee"));
        assertEquals(state.size(), 5);
    }

    @Test
    void variableDefinitionContainsEqualsSign() {
        Loader loader = new StandardLoader();
        assertThrows(ParsingException.class, () -> loader.load("a1;b=2;c=3"));
        assertDoesNotThrow(() -> loader.load("a=1;b=2;c=3;"));
        assertDoesNotThrow(() -> loader.load("a=1;b=2;c=3"));
    }

    @Test
    void variableDefinitionDoesNotBeginWithDigit() {
        Loader loader = new StandardLoader();
        assertThrows(ParsingException.class, () -> loader.load("0a=1"));
        assertDoesNotThrow(() -> loader.load("a0=1"));
    }

    @Test
    void loaderThrowsWhenNoExtractorAccepts() {
        Loader loader = new StandardLoader();
        final String variables = "a=$$";//$ is filtered before passing to parser or else it'd ruin html formatting
        assertThrows(ParsingException.class, () -> loader.load(variables));
    }

    @Test
    void correctVariablesPass() {
        Loader loader = new StandardLoader();
        final Map<String, Variable<?>>[] state = new Map[]{new TreeMap<>()};//cause lambda
        final String variables = "a=1;b=-1;c=1.1;d=-1.1;e=\"\";f=\"\"\";g=(1);h=(1,-1,1.1,-1.1);i=[1];j=[1/-1/1.1/-1.1];k=[1,-1/1.1,-1.1];l=[1,-1,1.1,-1.1]";
        assertDoesNotThrow(() -> state[0] = loader.load(variables));
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
}
