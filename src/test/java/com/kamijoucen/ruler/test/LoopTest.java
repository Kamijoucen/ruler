package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.module.RuleRunner;
import com.kamijoucen.ruler.parameter.RuleResult;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LoopTest {

    public RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
        configuration.setGlobalImportModule("/ruler/std/global.txt", "op");
    }

    // break测试
    @Test
    public void breakTest() {

        String script = "var i = 0; var r = while i < 10 { i = i + 1; if (i == 5) { break; } println(i);}; return i;";
        RuleRunner runner = Ruler.compileScript(script, configuration);
        RuleResult result = runner.run();
        Assert.assertEquals(5L, result.first().toInteger());
    }

    // continue测试
    @Test
    public void continueTest() {

        String script = "var i = 0; var r = while i < 10 { i = i + 1; if (i == 5) { return i; } println(i);};";
        RuleRunner runner = Ruler.compileScript(script, configuration);
        RuleResult result = runner.run();
        Assert.assertEquals(5, result.first().toInteger());
    }

    // return测试
    @Test
    public void returnTest() {

        String script = "var i = 0; var r = while i < 10 { i = i + 1; if (i == 5) { return i; } println(i);}; return i;";
        RuleRunner runner = Ruler.compileScript(script, configuration);
        RuleResult result = runner.run();
        Assert.assertEquals(5L, result.first().toInteger());
    }

    // break嵌套测试
    @Test
    public void breakNestTest() {

        String script = "var i = 0; var r = while i < 10 { i = i + 1; var j = 0; while j < 10 { j = j + 1; if (j == 5) { break; } println(j);}}; return i;";
        RuleRunner runner = Ruler.compileScript(script, configuration);
        RuleResult result = runner.run();
        Assert.assertEquals(10L, result.first().toInteger());
    }

    // array push测试
    @Test
    public void arrayPushTest() {

        String script = "var i = 0; var r = []; while i < 10 { i = i + 1; if (i == 5) { continue; } r.push(i);} return r;";
        RuleRunner runner = Ruler.compileScript(script, configuration);
        RuleResult result = runner.run();
        Assert.assertEquals("[1, 2, 3, 4, 6, 7, 8, 9, 10]", result.first().toString());
    }

}
