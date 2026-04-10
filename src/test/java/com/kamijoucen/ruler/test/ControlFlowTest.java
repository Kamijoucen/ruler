package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.module.RulerRunner;
import com.kamijoucen.ruler.parameter.RulerResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ControlFlowTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerRunner compileScript(String text) {
        return Ruler.compileScript(text, configuration);
    }

    // ---------- for-each ----------

    @Test
    public void testForEachArray() {
        String script = "var sum = 0; for v in [1, 2, 3, 4, 5] { sum = sum + v; } return sum;";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals(15L, r.first().toInteger());
    }

    @Test
    public void testForEachSingleStatement() {
        // ForEachParser is excluded from semicolon requirement.
        String script = "var sum = 0; for v in [1, 2, 3]: sum = sum + v; return sum;";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals(6L, r.first().toInteger());
    }

    @Test
    public void testForEachBreak() {
        String script = "var r = 0; for v in [1, 2, 3, 4, 5] { if v == 3 { break; } r = v; } return r;";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals(2L, r.first().toInteger());
    }

    @Test
    public void testForEachContinue() {
        String script = "var r = []; for v in [1, 2, 3, 4, 5] { if v == 3 { continue; } r.push(v); } return r;";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals("[1, 2, 4, 5]", r.first().toString());
    }

    @Test
    public void testForEachReturn() {
        String script = "fun f() { for v in [1, 2, 3] { if v == 2 { return v; } } return 0; } return f();";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals(2L, r.first().toInteger());
    }

    @Test
    public void testNestedForEachBreak() {
        String script = "var r = 0; for a in [1, 2] { for b in [10, 20] { if b == 20 { break; } r = r + b; } } return r;";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals(20L, r.first().toInteger());
    }

    // ---------- while single-statement ----------

    @Test
    public void testWhileSingleStatement() {
        // WhileParser is excluded from semicolon requirement.
        String script = "var i = 0; while i < 5: i = i + 1; return i;";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals(5L, r.first().toInteger());
    }

    // ---------- if/else if/else ----------

    @Test
    public void testIfElseIfElse() {
        String script = "var r = if false: 1; else if true: 2; else 3; ; return r;";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals(2L, r.first().toInteger());
    }

    @Test
    public void testIfBlockReturnsValue() {
        // IfParser needs a trailing semicolon before the next statement.
        String script = "var a = 5; var r = if a > 3 { 'big'; } else { 'small'; } ; return r;";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals("big", r.first().toString());
    }

    // ---------- return inside loops ----------

    @Test
    public void testReturnInsideWhile() {
        String script = "fun f() { var i = 0; while i < 10 { i = i + 1; if i == 5 { return i; } } return 0; } return f();";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals(5L, r.first().toInteger());
    }

    @Test
    public void testReturnInsideNestedLoop() {
        String script = "fun f() { while true { for v in [1, 2, 3] { if v == 2 { return v; } } } return 0; } return f();";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals(2L, r.first().toInteger());
    }

    // ---------- nested while break/continue ----------

    @Test
    public void testContinueInWhile() {
        String script = "var r = []; var i = 0; while i < 5 { i = i + 1; if i == 3 { continue; } r.push(i); } return r;";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals("[1, 2, 4, 5]", r.first().toString());
    }
}
