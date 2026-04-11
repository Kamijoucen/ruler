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

    @Test
    public void testBreakAndContinueWithoutSemicolon() {
        String script = "var r = []\n" +
                "for i in [1,2,3,4,5] {\n" +
                "    if i == 3 {\n" +
                "        continue\n" +
                "    }\n" +
                "    if i == 4 {\n" +
                "        break\n" +
                "    }\n" +
                "    r.push(i)\n" +
                "}\n" +
                "return r.length()";
        Assert.assertEquals(2L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testAssignmentWithoutSemicolon() {
        String script = "var a = 0\na = 1\na = a + 2\nreturn a";
        Assert.assertEquals(3L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testExpressionStatementWithoutSemicolon() {
        String script = "var a = 0\nprintln(a)\na = 1\nreturn a";
        Assert.assertEquals(1L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testReturnMultipleValuesWithNewline() {
        String script = "fun f() {\n" +
                "  return 1, 2\n" +
                "}\n" +
                "var r = f()\n" +
                "return r[1]";
        Assert.assertEquals(2L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testMultipleBlankLines() {
        String script = "var a = 1\n\n\nvar b = 2\nreturn a + b";
        Assert.assertEquals(3L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testArrayLiteralAsStatement() {
        String script = "[1,2,3]\nreturn 0";
        Assert.assertEquals(0L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testIfAbbreviationThenNewline() {
        String script = "if true: println(0)\nvar a = 1\nreturn a";
        Assert.assertEquals(1L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testChainCallSpanLines() {
        String script = "var obj = { getA: fun() { return 10; } }\n" +
                "var a = obj\n" +
                ".getA()\n" +
                "return a";
        Assert.assertEquals(10L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testFunctionArgsSpanLines() {
        String script = "fun add(a, b) { return a + b }\n" +
                "var a = add(\n" +
                "  1,\n" +
                "  2\n" +
                ")\n" +
                "return a";
        Assert.assertEquals(3L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testWhileBlockBreakWithoutSemicolon() {
        String script = "var i = 0\n" +
                "while i \u003c 10 {\n" +
                "  i = i + 1\n" +
                "  if i == 3 {\n" +
                "    break\n" +
                "  }\n" +
                "}\n" +
                "return i";
        Assert.assertEquals(3L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testCustomInfixChainedWithoutSemicolon() {
        String script = "infix fun push(arr, v) { arr.push(v); return arr; }\n" +
                "var arr = [1,2,3]\n" +
                "arr push 4 push 5\n" +
                "return arr.length()";
        Assert.assertEquals(5L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testInfixSpillPreventionInVarDefine() {
        String script = "infix fun push(arr, v) { arr.push(v); return arr; }\n" +
                "var arr = [1,2,3]\n" +
                "var x = arr push 4\n" +
                "return x.length()";
        Assert.assertEquals(4L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testReturnBeforeRightBraceWithoutSemicolon() {
        String script = "fun f() { return 1 }\nreturn f()";
        Assert.assertEquals(1L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testImportInfixWithoutSemicolon() {
        String script = "import infix \"/ruler/std/global.txt\" op\nreturn 0";
        Assert.assertEquals(0L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testIndexAccessSpanLines() {
        String script = "var arr = [10,20,30]\n" +
                "var a = arr[\n" +
                "  1\n" +
                "]\n" +
                "return a";
        Assert.assertEquals(20L, compileScript(script).run().first().toInteger());
    }

    @Test
    public void testTypeofSpanLines() {
        String script = "var t = typeof(\n" +
                "  1\n" +
                ")\n" +
                "return t === \"int\"";
        Assert.assertTrue(compileScript(script).run().first().toBoolean());
    }
}
