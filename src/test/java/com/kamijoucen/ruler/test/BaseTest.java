package com.kamijoucen.ruler.test;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.config.impl.CustomImportLoaderManagerImpl;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.module.RulerRunner;
import com.kamijoucen.ruler.parameter.RuleResultValue;
import com.kamijoucen.ruler.parameter.RulerResult;
import com.kamijoucen.ruler.test.option.FuncParamLengthTestFunction;
import com.kamijoucen.ruler.test.option.TestImportLoader1;
import com.kamijoucen.ruler.test.option.TestImportLoader2;
import com.kamijoucen.ruler.test.option.TestImportLoader3;

public class BaseTest {

    public RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
        configuration.registerGlobalImportPathModule("/ruler/std/global.txt", "op");
        configuration.registerGlobalFunction(new FuncParamLengthTestFunction());
    }

    public RulerRunner getExpressionRunner(String text) {
        return Ruler.compileExpression(text, configuration);
    }

    public RulerRunner getScriptRunner(String text) {
        return Ruler.compileScript(text, configuration);
    }

    @Test
    public void arrayInTest() {
        String script = "op.In($target, [99, 1.5, 5])";
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

    // 全等于测试
    @Test
    public void strictEqTest() {
        String script = "var a = 1; var b = 1; return a === b;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertTrue(result.first().toBoolean());
    }

    // 全等于测试2
    @Test
    public void strictEqTest2() {
        String script = "var a = 1; var b = 1.0; return a === b;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertTrue(result.first().toBoolean());
    }

    // 全等于测试3
    @Test
    public void strictEqTest3() {
        String script = "var a = 1; var b = '1'; return a === b;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertFalse(result.first().toBoolean());
    }

    // 等于测试
    @Test
    public void eqTest() {
        String script = "var a = 1; var b = 1; return a == b;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertTrue(result.first().toBoolean());
    }

    // 等于测试2
    @Test
    public void eqTest2() {
        String script = "var a = 1; var b = 1.0; return a == b;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertTrue(result.first().toBoolean());
    }

    // 等于测试3
    @Test
    public void eqTest3() {
        String script = "var a = 1; var b = '1'; return a == b;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertTrue(result.first().toBoolean());
    }

    // if 表达式测试
    @Test
    public void ifExpressionTest3() {
        String script = "var a = $`var`; var b = if a === 1: 1; else { 3; 2; 5; } ; return b;";
        RulerRunner runner = getScriptRunner(script);

        Map<String, Object> map = new HashMap<>();
        map.put("var", 9);

        RulerResult result = runner.run(map);
        Assert.assertEquals(5, result.first().toInteger());
    }

    // Call函数测试
    @Test
    public void callFunctionTest() {
        String script = "fun Test(n) { return n + 1; } var a = Call('Test')(100); return a;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertEquals(101, result.first().toInteger());
    }

    // ruler func param length test
    @Test
    public void funcParamLengthTest() {
        String script = "var a = FuncParamLengthTestFunction(1, 2, 3, 4, 5); return a;";
        RulerRunner runner = getScriptRunner(script);

        RulerResult result = runner.run();
        Assert.assertEquals(5, result.first().toInteger());
    }


    // import loader sort test
    @Test
    public void importLoaderSortTest() {
        CustomImportLoaderManagerImpl loaderManager = new CustomImportLoaderManagerImpl() {
            @Override
            public String load(String path) {

                for (int i = 0; i < super.loaders.size(); i++) {
                    if (i == 0) {
                        // 3 的优先级最高，数字最大，因此会排第一
                        Assert.assertEquals("3", super.loaders.get(i).getLoader().load(null));
                    } else if (i == 1) {
                        Assert.assertEquals("2", super.loaders.get(i).getLoader().load(null));
                    } else if (i == 2) {
                        Assert.assertEquals("1", super.loaders.get(i).getLoader().load(null));
                    }
                }
                return null;
            }

        };

        loaderManager.registerCustomImportLoader(new TestImportLoader1());
        loaderManager.registerCustomImportLoader(new TestImportLoader2());
        loaderManager.registerCustomImportLoader(new TestImportLoader3());

        loaderManager.load(null);
    }

}
