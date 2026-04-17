package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.exception.PanicException;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
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

    private RulerRunner compile(String text) {
        return Ruler.compile(text, configuration);
    }

    // ---------- max loop number ----------

    @Test(expected = RulerRuntimeException.class)
    public void testMaxLoopNumberExceededWhile() {
        configuration.setMaxLoopNumber(3);
        String script = "var i = 0; while true { i = i + 1; }";
        compile(script).run();
    }

    @Test(expected = RulerRuntimeException.class)
    public void testMaxLoopNumberExceededForEach() {
        configuration.setMaxLoopNumber(2);
        String script = "for v in [1, 2, 3, 4, 5] { println(v); }";
        compile(script).run();
    }

    // ---------- max stack depth ----------

    @Test(expected = RulerRuntimeException.class)
    public void testMaxStackDepthExceeded() {
        configuration.setMaxStackDepth(5);
        String script = "var f = fun(n) { if n <= 1 { return 1; } return n * f(n - 1); }; return f(10);";
        compile(script).run();
    }

    // ---------- panic ----------

    @Test(expected = PanicException.class)
    public void testPanic() {
        compile("Panic('something wrong');").run();
    }

    // ---------- undefined variable ----------

    @Test(expected = RulerRuntimeException.class)
    public void testUndefinedVariable() {
        compile("return x;").run();
    }

    // ---------- redeclare variable ----------

    @Test(expected = RulerRuntimeException.class)
    public void testRedeclareVariable() {
        compile("var a = 1; var a = 2;").run();
    }

    // ---------- calling non-function ----------

    @Test(expected = RulerRuntimeException.class)
    public void testCallNonFunction() {
        compile("var a = 1; a();").run();
    }

    // ---------- if condition non-boolean ----------

    @Test(expected = RulerRuntimeException.class)
    public void testIfConditionNonBoolean() {
        compile("if 1 { return 1; }").run();
    }

    // ---------- invalid assignment target ----------

    @Test(expected = SyntaxException.class)
    public void testInvalidLhsAssignment() {
        compile("1 = 2;").run();
    }

    // ---------- division by zero throws ArithmeticException ----------

    @Test(expected = ArithmeticException.class)
    public void testDivByZeroThrows() {
        RulerConfigurationImpl cfg = new RulerConfigurationImpl();
        Ruler.compile("1 / 0", cfg).run();
    }
}
