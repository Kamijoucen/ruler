package com.kamijoucen.ruler.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.module.RuleRunner;
import com.kamijoucen.ruler.parameter.RuleResult;
import com.kamijoucen.ruler.parameter.RuleResultValue;

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

    @Test
    public void arrayPushTest() {
        String script = "var a = [1, 2, 3]; println(a); return a.length();";
        RuleRunner runner = getScriptRunner(script);

        RuleResult result = runner.run();

        Assert.assertEquals(3, result.first().toInteger());
    }

    @Test
    public void ifExpressionTest() {
        String script = "var r = if 15 > 111: 'a'; else 'b'; ; return r;";
        RuleRunner runner = getScriptRunner(script);

        RuleResult result = runner.run();

        Assert.assertEquals("b", result.first().toString());
    }

    @Test
    public void ifExpressionTest2() {
        String script = "var r = if 15 > 100: 'a'; else if 15 < 100: 'b';; return r;";
        RuleRunner runner = getScriptRunner(script);

        RuleResult result = runner.run();

        Assert.assertEquals("b", result.first().toString());
    }

    @Test
    public void anonymousFuncTest() {
        String script = "var f = fun(name) -> \"hello \" + name;\n" +
                "\n" +
                "var r = f(\"world!\");\n" +
                "println(r);\n" +
                "\n" +
                "return r;";
        RuleRunner runner = getScriptRunner(script);
        RuleResult result = runner.run();

        Assert.assertEquals("hello world!", result.first().toString());
    }

    @Test
    public void outStringIdenTest() {

        String script = "var `t  test`= $`123 `; return `t  test`;";

        Map<String, Object> map = new HashMap<>();
        map.put("123 ", "hello world!");

        RuleRunner runner = getScriptRunner(script);
        RuleResult result = runner.run(map);

        Assert.assertEquals("hello world!", result.first().toString());
    }

    // 数组赋值测试
    @Test
    public void arrayAssignTest() {
        String script = "var a = [1, 2, 3]; a[1] = 5; return a[1];";
        RuleRunner runner = getScriptRunner(script);

        RuleResult result = runner.run();

        Assert.assertEquals(5, result.first().toInteger());
    }

    // 对象赋值测试
    @Test
    public void objectAssignTest() {
        String script = "var a = {name: 'lisicen', age: 18}; a.name = 'lisicen2'; return a.name;";
        RuleRunner runner = getScriptRunner(script);

        RuleResult result = runner.run();

        Assert.assertEquals("lisicen2", result.first().toString());
    }

    // 嵌套对象赋值测试
    @Test
    public void objectAssignTest2() {
        String script = "var a = {name: 'lisicen', age: 18, obj: {name: 'lisicen2'}}; a.obj.name = 'lisicen3'; return a.obj.name;";
        RuleRunner runner = getScriptRunner(script);

        RuleResult result = runner.run();

        Assert.assertEquals("lisicen3", result.first().toString());
    }

    @Test
    public void mapGetTest() {
        String script = "var a = $obj['name']; println(a); return a;";
        RuleRunner runner = getScriptRunner(script);

        Map<String, Object> parameter = new HashMap<String, Object>();
        Map<String, String> obj = new HashMap<String, String>();
        obj.put("name", "lisicen");
        parameter.put("obj", obj);

        RuleResult result = runner.run(parameter);
        Assert.assertEquals("lisicen", result.first().toString());
    }

    // 使用proxy为数组拦截length方法
    @Test
    public void arrayLengthTest() {
        String script = "var a = [1, 2, 3]; a = Proxy(a, {get: fun(self, name) { return self.length() + 1; }});  return a.length;";
        RuleRunner runner = getScriptRunner(script);

        RuleResult result = runner.run();
        Assert.assertEquals(4, result.first().toInteger());
    }

    // 全等于测试
    @Test
    public void strictEqTest() {
        String script = "var a = 1; var b = 1; return a === b;";
        RuleRunner runner = getScriptRunner(script);

        RuleResult result = runner.run();
        Assert.assertEquals(true, result.first().toBoolean());
    }
    
    // 全等于测试2
    @Test
    public void strictEqTest2() {
        String script = "var a = 1; var b = 1.0; return a === b;";
        RuleRunner runner = getScriptRunner(script);

        RuleResult result = runner.run();
        Assert.assertEquals(true, result.first().toBoolean());
    }

    // 全等于测试3
    @Test
    public void strictEqTest3() {
        String script = "var a = 1; var b = '1'; return a === b;";
        RuleRunner runner = getScriptRunner(script);

        RuleResult result = runner.run();
        Assert.assertEquals(false, result.first().toBoolean());
    }

    // 等于测试
    @Test
    public void eqTest() {
        String script = "var a = 1; var b = 1; return a == b;";
        RuleRunner runner = getScriptRunner(script);

        RuleResult result = runner.run();
        Assert.assertEquals(true, result.first().toBoolean());
    }
    
    // 等于测试2
    @Test
    public void eqTest2() {
        String script = "var a = 1; var b = 1.0; return a == b;";
        RuleRunner runner = getScriptRunner(script);

        RuleResult result = runner.run();
        Assert.assertEquals(true, result.first().toBoolean());
    }

    // 等于测试3
    @Test
    public void eqTest3() {
        String script = "var a = 1; var b = '1'; return a == b;";
        RuleRunner runner = getScriptRunner(script);

        RuleResult result = runner.run();
        Assert.assertEquals(true, result.first().toBoolean());
    }
    
    // if 表达式测试
    @Test
    public void ifExpressionTest3() {
        String script = "var a = $`var`; var b = if a === 1: 1; else { 3; 2; 5; } ; return b;";
        RuleRunner runner = getScriptRunner(script);

        Map<String, Object> map = new HashMap<>();
        map.put("var", 9);
        
        RuleResult result = runner.run();
        Assert.assertEquals(5, result.first().toInteger());
    }

    // Call函数测试
    @Test
    public void callFunctionTest() {
        String script = "fun Test(n) { return n + 1; } var a = Call('Test')(100); return a;";
        RuleRunner runner = getScriptRunner(script);

        RuleResult result = runner.run();
        Assert.assertEquals(101, result.first().toInteger());
    }
    
}
