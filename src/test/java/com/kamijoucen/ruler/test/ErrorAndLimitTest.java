package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.exception.PanicException;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.service.RulerRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ErrorAndLimitTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerRunner compileScript(String text) {
        return Ruler.compileScript(text, configuration);
    }

    // ---------- max loop number ----------

    @Test(expected = RuntimeException.class)
    public void testMaxLoopNumberExceededWhile() {
        configuration.setMaxLoopNumber(3);
        String script = "var i = 0; while true { i = i + 1; }";
        compileScript(script).run();
    }

    @Test(expected = RuntimeException.class)
    public void testMaxLoopNumberExceededForEach() {
        configuration.setMaxLoopNumber(2);
        String script = "for v in [1, 2, 3, 4, 5] { println(v); }";
        compileScript(script).run();
    }

    // ---------- max stack depth ----------

    @Test(expected = RuntimeException.class)
    public void testMaxStackDepthExceeded() {
        configuration.setMaxStackDepth(5);
        String script = "var f = fun(n) { if n <= 1 { return 1; } return n * f(n - 1); }; return f(10);";
        compileScript(script).run();
    }

    // ---------- panic ----------

    @Test(expected = PanicException.class)
    public void testPanic() {
        compileScript("Panic('something wrong');").run();
    }

    // ---------- undefined variable ----------

    @Test(expected = SyntaxException.class)
    public void testUndefinedVariable() {
        compileScript("return x;").run();
    }

    // ---------- redeclare variable ----------

    @Test(expected = SyntaxException.class)
    public void testRedeclareVariable() {
        compileScript("var a = 1; var a = 2;").run();
    }

    // ---------- calling non-function ----------

    @Test(expected = IllegalArgumentException.class)
    public void testCallNonFunction() {
        compileScript("var a = 1; a();").run();
    }

    // ---------- if condition non-boolean ----------

    @Test(expected = SyntaxException.class)
    public void testIfConditionNonBoolean() {
        compileScript("if 1 { return 1; }").run();
    }

    // ---------- invalid assignment target ----------

    @Test(expected = SyntaxException.class)
    public void testInvalidLhsAssignment() {
        compileScript("1 = 2;").run();
    }

    // ---------- division by zero produces infinity (not exception) ----------

    @Test
    public void testDivByZeroIsInfinity() {
        RulerConfigurationImpl cfg = new RulerConfigurationImpl();
        com.kamijoucen.ruler.domain.parameter.RulerResult r = Ruler.compileExpression("1 / 0", cfg).run();
        Assert.assertTrue(Double.isInfinite(r.first().toDouble()));
    }
}
