package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.service.RulerRunner;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OptionalSemicolonTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerRunner compileScript(String text) {
        return Ruler.compileScript(text, configuration);
    }

    @Test
    public void testNewlineReplacesSemicolon() {
        String script = "var a = 1\nvar b = 2\nreturn a + b";
        Assert.assertEquals(3L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testSemicolonStillWorks() {
        String script = "var a = 1; var b = 2; return a + b;";
        Assert.assertEquals(3L, compileScript(script).run().first().toInteger());
    }

    @Test(expected = SyntaxException.class)
    public void testSameLineWithoutSemicolonFails() {
        String script = "var a = 1 var b = 2";
        compileScript(script).run();
    }

    @Test
    public void testReturnEndsAtNewline() {
        String script = "fun f() {\n  return 1\n  2\n}\nreturn f();";
        Assert.assertEquals(1L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testBuiltinOperatorCanSpanLines() {
        String script = "var a = 1 +\n2\nreturn a";
        Assert.assertEquals(3L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testCustomInfixBreaksAtNewline() {
        String script = "infix fun push(arr, v) { arr.push(v); return arr; }\n" +
                "var arr = [1,2,3]\n" +
                "arr push 4\n" +
                "return arr.length();";
        Assert.assertEquals(4L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testImportWithoutSemicolon() {
        String script = "import \"/ruler/std/global.txt\" op\nreturn op.Add(1,2,3)";
        Assert.assertEquals(6L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testNoSemicolonBeforeRightBrace() {
        String script = "if true {\n  var a = 1\n  var b = 2\n}\nreturn 0";
        Assert.assertEquals(0L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testNoSemicolonAtEndOfFile() {
        String script = "var a = 5\nreturn a";
        Assert.assertEquals(5L, compileScript(script).run().first().toInteger());
    }
}
