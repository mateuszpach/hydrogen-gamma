package model.modules.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FunctionsTest {

    @Test
    public void subcomponentsTest() {
        String f1 = "sin(x)*cos(x)/17";
        String f2 = "x+e^sin(x)*cos(x)/17";
        String f3 = "sin(x)^sin(x)^sin(x)";
        String f4 = "a*b+c/d";
        String f5 = "x";
        List<String> ef1 = Arrays.asList("sin(x)", "cos(x)", "17");
        List<Character> eo1 = Arrays.asList('*', '/');
        List<String> ef2 = Arrays.asList("x+e^sin(x)*cos(x)", "17");
        List<Character> eo2 = Arrays.asList('/');
        List<String> ef3 = Arrays.asList("sin(x)", "sin(x)", "sin(x)");
        List<Character> eo3 = Arrays.asList('^', '^');
        List<String> ef4 = Arrays.asList("a", "b", "c", "d");
        List<Character> eo4 = Arrays.asList('*', '+', '/');
        List<String> ef5 = Arrays.asList("x");
        List<Character> eo5 = Arrays.asList();

        var s1 = Functions.findSubcomponents(f1, "*/");
        var s2 = Functions.findSubcomponents(f2, "/");
        var s3 = Functions.findSubcomponents(f3, "^");
        var s4 = Functions.findSubcomponents(f4, "*+/");
        var s5 = Functions.findSubcomponents(f5, "-+*/^");

        assertEquals(ef1, s1.first);
        assertEquals(eo1, s1.second);
        assertEquals(ef2, s2.first);
        assertEquals(eo2, s2.second);
        assertEquals(ef3, s3.first);
        assertEquals(eo3, s3.second);
        assertEquals(ef4, s4.first);
        assertEquals(eo4, s4.second);
        assertEquals(ef5, s5.first);
        assertEquals(eo5, s5.second);
    }
}