package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.module.RuleRunner;
import com.kamijoucen.ruler.parameter.RuleResult;
import com.kamijoucen.ruler.parameter.RuleResultValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    public RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
        configuration.setGlobalImportModule("/ruler/std/global.txt", "op");
    }

    public RuleRunner getExpressionRunner(String text) {
        return Ruler.compileExpression(text, configuration);
    }

    public RuleRunner getScriptRunner(String text) {
        return Ruler.compileScript(text, configuration);
    }

    @Test
    public void arrayInTest() {
        String script = "op.In($target, [99, 1.5, 5])";
        RuleRunner runner = getExpressionRunner(script);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("target", 1.5);
        RuleResult run = runner.run(param);

        Assert.assertEquals(1, run.size());
        RuleResultValue value = run.getResult().get(0);
        Assert.assertEquals(true, value.toBoolean());

        param.put("target", "99");
        run = runner.run(param);
        Assert.assertEquals(1, run.size());
        value = run.getResult().get(0);
        Assert.assertEquals(true, value.toBoolean());

        param.put("target", "null");
        run = runner.run(param);
        Assert.assertEquals(1, run.size());
        value = run.getResult().get(0);
        Assert.assertEquals(false, value.toBoolean());
    }

    @Test
    public void mapConvertTest() {
        String script = "var a = $obj.name; println(a); return a;";
        RuleRunner runner = getScriptRunner(script);

        Map<String, Object> parameter = new HashMap<String, Object>();
        Map<String, String> obj = new HashMap<String, String>();
        obj.put("name", "lisicen");
        parameter.put("obj", obj);

        RuleResult result = runner.run(parameter);
        Assert.assertEquals("lisicen", result.first().toString());
    }

}
