package com.kamijoucen.ruler.test;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.kamijoucen.ruler.api.Ruler;
import com.kamijoucen.ruler.types.module.ModuleState;
import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.api.RulerRunner;
import com.kamijoucen.ruler.types.parameter.RuleResultValue;
import com.kamijoucen.ruler.types.parameter.RulerResult;
import com.kamijoucen.ruler.test.option.FuncParamLengthTestFunction;
import com.kamijoucen.ruler.test.option.TestImportLoader1;
import com.kamijoucen.ruler.test.option.TestImportLoader2;
import com.kamijoucen.ruler.test.option.TestImportLoader3;

public class BaseTest {

    public RulerConfiguration configuration;

    @Before
    public void init() {
        configuration = new RulerConfiguration();
        configuration.registerGlobalFunction(new FuncParamLengthTestFunction());
    }

    public RulerRunner getExpressionRunner(String text) {
        return Ruler.compile(text, configuration);
    }

    public RulerRunner getScriptRunner(String text) {
        return Ruler.compile(text, configuration);
    }

    @Test
    public void arrayInTest() {
        configuration.registerGlobalImportPathModule("/ruler/std/collections.txt", "listUtil");
        String script = "listUtil.In($target, [99, 1.5, 5])";
        RulerRunner runner = getExpressionRunner(script);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("target", 1.5);
        RulerResult run = runner.run(param);

        Assert.assertEquals(1, run.size());
        RuleResultValue value = run.getResult().get(0);
        Assert.assertTrue(value.toBoolean());

        param.put("target", "99");
        run = runner.run(param);
        Assert.assertEquals(1, run.size());
        value = run.getResult().get(0);
        Assert.assertTrue(value.toBoolean());

        param.put("target", "null");
        run = runner.run(param);
        Assert.assertEquals(1, run.size());
        value = run.getResult().get(0);
        Assert.assertFalse(value.toBoolean());
    }

    @Test
    public void mapConvertTest() {
        String script = "var a = $obj.name; println(a); return a;";
        RulerRunner runner = getScriptRunner(script);

        Map<String, Object> parameter = new HashMap<String, Object>();
        Map<String, String> obj = new HashMap<String, String>();
        obj.put("name", "lisicen");
        parameter.put("obj", obj);

        RulerResult result = runner.run(parameter);
        Assert.assertEquals("lisicen", result.first().toString());
    }

    @Test
    public void arrayPushTest() {
        String script = "var a = [1, 2, 3]; println(a); return a.length();";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();

        Assert.assertEquals(3, result.first().toInteger());
    }

    @Test
    public void ifExpressionTest() {
        String script = "var r = if 15 > 111: 'a'; else 'b'; ; return r;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();

        Assert.assertEquals("b", result.first().toString());
    }

    @Test
    public void ifExpressionTest2() {
        String script = "var r = if 15 > 100: 'a'; else if 15 < 100: 'b';; return r;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();

        Assert.assertEquals("b", result.first().toString());
    }

    @Test
    public void anonymousFuncTest() {
        String script = "var f = fun(name) -> \"hello \" ++ name;\n" + "\n"
                + "var r = f(\"world!\");\n" + "println(r);\n" + "\n" + "return r;";
        RulerRunner runner = getScriptRunner(script);
        RulerResult result = runner.run();

        Assert.assertEquals("hello world!", result.first().toString());
    }

    @Test
    public void outStringIdenTest() {

        String script = "var `t  test`= $`123 `; return `t  test`;";

        Map<String, Object> map = new HashMap<>();
        map.put("123 ", "hello world!");

        RulerRunner runner = getScriptRunner(script);
        RulerResult result = runner.run(map);

        Assert.assertEquals("hello world!", result.first().toString());
    }

    // 数组赋值测试
    @Test
    public void arrayAssignTest() {
        String script = "var a = [1, 2, 3]; a[1] = 5; return a[1];";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();

        Assert.assertEquals(5, result.first().toInteger());
    }

    // 对象赋值测试
    @Test
    public void objectAssignTest() {
        String script = "var a = {name: 'lisicen', age: 18}; a.name = 'lisicen2'; return a.name;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();

        Assert.assertEquals("lisicen2", result.first().toString());
    }

    // 嵌套对象赋值测试
    @Test
    public void objectAssignTest2() {
        String script =
                "var a = {name: 'lisicen', age: 18, obj: {name: 'lisicen2'}}; a.obj.name = 'lisicen3'; return a.obj.name;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();

        Assert.assertEquals("lisicen3", result.first().toString());
    }

    @Test
    public void mapGetTest() {
        String script = "var a = $obj['name']; println(a); return a;";
        RulerRunner runner = getScriptRunner(script);

        Map<String, Object> parameter = new HashMap<String, Object>();
        Map<String, String> obj = new HashMap<String, String>();
        obj.put("name", "lisicen");
        parameter.put("obj", obj);

        RulerResult result = runner.run(parameter);
        Assert.assertEquals("lisicen", result.first().toString());
    }

    // 使用proxy为数组拦截length方法
    @Test
    public void arrayLengthTest() {
        String script =
                "var a = [1, 2, 3]; a = Proxy(a, {get: fun(self, name) { return self.length() + 1; }});  return a.length;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertEquals(4, result.first().toInteger());
    }

    @Test
    public void strictEqTest() {
        String script = "var a = 1; var b = 1; return a === b;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertTrue(result.first().toBoolean());
    }

    @Test
    public void strictEqTest2() {
        String script = "var a = 1; var b = 1.0; return a === b;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertTrue(result.first().toBoolean());
    }

    @Test
    public void strictEqTest3() {
        String script = "var a = 1; var b = '1'; return a === b;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertFalse(result.first().toBoolean());
    }

    @Test
    public void eqTest() {
        String script = "var a = 1; var b = 1; return a == b;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertTrue(result.first().toBoolean());
    }

    @Test
    public void eqTest2() {
        String script = "var a = 1; var b = 1.0; return a == b;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertTrue(result.first().toBoolean());
    }

    @Test
    public void eqTest3() {
        String script = "var a = 1; var b = '1'; return a == b;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertTrue(result.first().toBoolean());
    }

    @Test
    public void ifExpressionTest3() {
        String script = "var a = $`var`; var b = if a === 1: 1; else { 3; 2; 5; } ; return b;";
        RulerRunner runner = getScriptRunner(script);

        Map<String, Object> map = new HashMap<>();
        map.put("var", 9);

        RulerResult result = runner.run(map);
        Assert.assertEquals(5, result.first().toInteger());
    }

    @Test
    public void callFunctionTest() {
        String script = "fun Test(n) { return n + 1; } var a = Call('Test')(100); return a;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertEquals(101, result.first().toInteger());
    }

    @Test
    public void funcParamLengthTest() {
        String script = "var a = FuncParamLengthTestFunction(1, 2, 3, 4, 5); return a;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertEquals(5, result.first().toInteger());
    }


    @Test
    public void importLoaderSortTest() {
        ModuleState modules = new ModuleState();
        modules.registerLoader(new TestImportLoader1());
        modules.registerLoader(new TestImportLoader2());
        modules.registerLoader(new TestImportLoader3());
        Assert.assertEquals("3", modules.getLoaders().get(0).load(null));
        Assert.assertEquals("2", modules.getLoaders().get(1).load(null));
        Assert.assertEquals("1", modules.getLoaders().get(2).load(null));
    }

    @Test
    public void stringBlockTest() {
        String script = "var a = \"\"\"hello\n\"world\"\n\"\"\"; return a;";
        RulerRunner runner = getScriptRunner(script);
        RulerResult result = runner.run();
        Assert.assertEquals("hello\n\"world\"\n", result.first().toString());
    }

    @Test
    public void stringBlockTest2() {
        String script = "var a = \"\"\"hello\n\"world\"\n\"\"\"; return a.length();";
        RulerRunner runner = getScriptRunner(script);
        RulerResult result = runner.run();
        Assert.assertEquals(14, result.first().toInteger());
    }

}
