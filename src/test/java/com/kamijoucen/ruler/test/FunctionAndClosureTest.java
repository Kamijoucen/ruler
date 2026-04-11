package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.service.RulerRunner;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FunctionAndClosureTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerRunner compile(String text) {
        return Ruler.compile(text, configuration);
    }

    // ---------- default params ----------

    @Test
    public void testDefaultParam() {
        String script = "fun Add(a, b = 10) { return a + b; } return Add(5);";
        RulerResult r = compile(script).run();
        Assert.assertEquals(15L, r.first().toInteger());
    }

    @Test
    public void testDefaultParamOverride() {
        String script = "fun Add(a, b = 10) { return a + b; } return Add(5, 3);";
        RulerResult r = compile(script).run();
        Assert.assertEquals(8L, r.first().toInteger());
    }

    // ---------- static capture ----------

    @Test
    public void testStaticCapture() {
        String script = "var a = 1; var f = fun[a]() { return a; }; a = 2; return f();";
        RulerResult r = compile(script).run();
        Assert.assertEquals(1L, r.first().toInteger());
    }

    @Test
    public void testStaticCaptureMultiple() {
        String script = "var a = 1; var b = 2; var f = fun[a, b]() { return a + b; }; a = 10; b = 20; return f();";
        RulerResult r = compile(script).run();
        Assert.assertEquals(3L, r.first().toInteger());
    }

    // ---------- multi-return ----------

    @Test
    public void testMultiReturn() {
        String script = "fun f() { return 1, 2, 3; } var r = f(); return r[0] + r[1] + r[2];";
        RulerResult r = compile(script).run();
        Assert.assertEquals(6L, r.first().toInteger());
    }

    @Test
    public void testMultiReturnIsArray() {
        String script = "fun f() { return 1, 'a'; } var r = f(); return r.length();";
        RulerResult r = compile(script).run();
        Assert.assertEquals(2L, r.first().toInteger());
    }

    // ---------- named function ----------

    @Test
    public void testNamedFunction() {
        String script = "fun Square(n) { return n * n; } return Square(5);";
        RulerResult r = compile(script).run();
        Assert.assertEquals(25L, r.first().toInteger());
    }

    // ---------- _args_ ----------

    @Test
    public void testArgsArray() {
        String script = "fun f() { return _args_.length(); } return f(1, 2, 3);";
        RulerResult r = compile(script).run();
        Assert.assertEquals(3L, r.first().toInteger());
    }

    @Test
    public void testArgsContents() {
        String script = "fun f() { return _args_[1]; } return f(10, 20, 30);";
        RulerResult r = compile(script).run();
        Assert.assertEquals(20L, r.first().toInteger());
    }

    // ---------- recursion ----------

    @Test
    public void testRecursionFibonacci() {
        String script = "var fib = fun(n) { if n <= 1 { return n; } return fib(n - 1) + fib(n - 2); }; return fib(10);";
        RulerResult r = compile(script).run();
        Assert.assertEquals(55L, r.first().toInteger());
    }

    @Test
    public void testRecursionFactorial() {
        String script = "var fact = fun(n) { if n <= 1 { return 1; } return n * fact(n - 1); }; return fact(5);";
        RulerResult r = compile(script).run();
        Assert.assertEquals(120L, r.first().toInteger());
    }

    // ---------- higher-order ----------

    @Test
    public void testFunctionAsArgument() {
        String script = "fun apply(f, x) { return f(x); } var double = fun(n) { return n * 2; }; return apply(double, 5);";
        RulerResult r = compile(script).run();
        Assert.assertEquals(10L, r.first().toInteger());
    }

    @Test
    public void testFunctionAsReturnValue() {
        String script = "fun makeAdder(a) { return fun(b) { return a + b; }; } var add5 = makeAdder(5); return add5(3);";
        RulerResult r = compile(script).run();
        Assert.assertEquals(8L, r.first().toInteger());
    }

    @Test
    public void testFunctionInArray() {
        String script = "var arr = [fun() { return 1; }, fun() { return 2; }]; return arr[1]();";
        RulerResult r = compile(script).run();
        Assert.assertEquals(2L, r.first().toInteger());
    }

    // ---------- method binding ----------

    @Test
    public void testMethodDetachAndCall() {
        String script = "var obj = { name: 'ruler', getName: fun(self) { return self.name; } }; var m = obj.getName; return m();";
        RulerResult r = compile(script).run();
        Assert.assertEquals("ruler", r.first().toString());
    }

    @Test
    public void testMethodWithExtraArgs() {
        String script = "var obj = { add: fun(self, a, b) { return a + b; } }; return obj.add(3, 4);";
        RulerResult r = compile(script).run();
        Assert.assertEquals(7L, r.first().toInteger());
    }

    // ---------- implicit return ----------

    @Test
    public void testImplicitReturnExpression() {
        String script = "fun f() { 42; } return f();";
        RulerResult r = compile(script).run();
        Assert.assertEquals(42L, r.first().toInteger());
    }

    @Test
    public void testImplicitReturnVarDefine() {
        String script = "fun f() { var a = 10; } return f();";
        RulerResult r = compile(script).run();
        Assert.assertEquals(10L, r.first().toInteger());
    }

    @Test
    public void testExplicitEmptyReturnStillNull() {
        String script = "fun f() { return; } return f();";
        RulerResult r = compile(script).run();
        Assert.assertEquals(1, r.size());
        Assert.assertNull(r.first().getValue());
    }

    // ---------- arrow function ----------

    @Test
    public void testArrowFunction() {
        String script = "var f = fun(x) -> x * x; return f(4);";
        RulerResult r = compile(script).run();
        Assert.assertEquals(16L, r.first().toInteger());
    }

    @Test
    public void testArrowFunctionClosure() {
        String script = "var a = 10; var f = fun(x) -> a + x; return f(5);";
        RulerResult r = compile(script).run();
        Assert.assertEquals(15L, r.first().toInteger());
    }
}
