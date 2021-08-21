package hydrogengamma.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionLatexTest {

    @Test
    public void simpleTest1() {
        String f1 = "sin(x)*cos(x)/17";
        String f2 = "x+e^sin(x)*cos(x)/17";
        String f3 = "sin(x)^sin(x)^sin(x)";
        String f4 = "a*b+c/d";
        String f5 = "x";


        Assertions.assertEquals("\\frac{ \\sin(x) \\dot \\cos(x) }{ 17 }", FunctionLatex.latexForm(f1));
        assertEquals("x + \\frac{ e^{ \\sin(x) } \\dot \\cos(x) }{ 17 }", FunctionLatex.latexForm(f2));
        assertEquals("\\sin(x)^{ \\sin(x) }^{ \\sin(x) }", FunctionLatex.latexForm(f3));
        assertEquals("a \\dot b + \\frac{ c }{ d }", FunctionLatex.latexForm(f4));
        assertEquals("x", FunctionLatex.latexForm(f5));
    }
}