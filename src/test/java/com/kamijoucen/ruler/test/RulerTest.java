package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.parameter.RulerParameter;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.logic.function.RulerFunction;
import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.service.RulerRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulerTest {

    public RulerConfigurationImpl configuration;

    @Before
    public void begin() {
        configuration = new RulerConfigurationImpl();
        configuration.registerGlobalImportPathModule("/ruler/std/util.txt", "util");
        configuration.registerGlobalImportPathModule("/ruler/std/collections.txt", "listUtil");
        configuration.registerGlobalImportScriptModule("var Ok = fun() { return 'OK!!!'; };", "ok");
        configuration.setMaxLoopNumber(5);
    }

    @Test
    public void test7() {
        String str = "var a = 0; if false { a = 1; } else if true { a = 2; } else { a = 3; } return a;";
        RulerRunner runner = Ruler.compile(str, configuration);
        Assert.assertEquals(2L, runner.run().first().toInteger());
    }

    @Test
    public void test8() {
        String str = "var str = ''; if 5 < 5 { str = '123123'; } else { str = makeItPossible(); } return typeof(str);";
        RulerRunner runner = Ruler.compile(str, configuration);
        Assert.assertEquals("string", runner.run().first().toString());
    }

    @Test
    public void test9() {
        String str = "var i = 15; return i;";
        RulerRunner runner = Ruler.compile(str, configuration);
        Assert.assertEquals(15L, runner.run().first().toInteger());
    }

    @Test
    public void test_rson_parse() {
        String str = "var rson = {name:'name', age:1, doit:fun() { return 'go'; }}; return rson.name ++ ':' ++ rson.doit();";
        RulerRunner script = Ruler.compile(str, configuration);
        Assert.assertEquals("name:go", script.run().first().toString());
    }

    @Test
    public void test_rson_parse2() {
        String str = "var rson = {f: {ff: fun() { return 'go!'; }}}; return rson.f.ff();";
        RulerRunner script = Ruler.compile(str, configuration);
        Assert.assertEquals("go!", script.run().first().toString());
    }

    @Test
    public void test_dot_call2() {
        String str = "var test = 'value'; var name = {getAge: fun() { return 19, test; }, name: 'hello'}; var result = name.getAge(); return [name.name, result[0], result[1]];";
        RulerRunner script = Ruler.compile(str, configuration);
        List<?> values = (List<?>) script.run().first().getValue();
        Assert.assertEquals("hello", values.get(0));
        Assert.assertEquals(java.math.BigInteger.valueOf(19), values.get(1));
        Assert.assertEquals("value", values.get(2));
    }

    @Test
    public void test_rson_this() {
        String str = "var a = {f: fun(self, age) { return self.name ++ age; }, name: 'name-'}; return a.f(18);";
        RulerRunner script = Ruler.compile(str, configuration);
        Assert.assertEquals("name-18", script.run().first().toString());
    }

    @Test
    public void out_name() {
        String str = "return ({a:1}).a;";
        RulerRunner script = Ruler.compile(str, configuration);
        Assert.assertEquals(1L, script.run().first().toInteger());
    }

    @Test
    public void typeof_test() {
        String str = "return [typeof('15'), typeof(fun() {}), typeof(1), typeof(1.0), typeof(println)];";
        RulerRunner script = Ruler.compile(str, configuration);
        List<?> result = (List<?>) script.run().first().getValue();
        Assert.assertEquals("string", result.get(0));
        Assert.assertEquals("function", result.get(1));
        Assert.assertEquals("int", result.get(2));
        Assert.assertEquals("double", result.get(3));
        Assert.assertEquals("function", result.get(4));
    }

    @Test
    public void null_input_test() {
        String script = "$name === null";
        RulerRunner runner = Ruler.compile(script, configuration);
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("name", null);
        RulerResult result = runner.run(args);
        Assert.assertTrue(result.first().toBoolean());
    }

    @Test
    public void array_call() {
        String script = "var arr = [[1]]; return arr[0].length();";
        RulerRunner run = Ruler.compile(script, configuration);
        Assert.assertEquals(1L, run.run().first().toInteger());
    }

    @Test
    public void import_test() {
        String script = "import '/ruler/std/sort.txt' sort; var arr = [2, 1, 85, 15,3]; sort.Sort(arr); println(arr);";
        RulerRunner run = Ruler.compile(script, configuration);
        run.run();
    }

    @Test
    public void custom_function() {
        configuration.registerGlobalFunction(new RulerFunction() {
            @Override
            public String getName() {
                return "哈哈哈哈";
            }

            @Override
            public Object call(RuntimeContext context, Scope scope, BaseValue self, Object... param) {
                return "我们gg啦！！！";
            }
        });

        String text = "return [哈哈哈哈(), typeof(哈哈哈哈)];";
        RulerRunner run = Ruler.compile(text, configuration);
        List<?> result = (List<?>) run.run().first().getValue();
        Assert.assertEquals("我们gg啦！！！", result.get(0));
        Assert.assertEquals("function", result.get(1));
    }

    @Test
    public void in_arr_test() {
        String script = "var arr = [2, 1, 85, 15,3]; println(listUtil.In(825, arr));";
        RulerRunner run = Ruler.compile(script, configuration);
        run.run();
    }

    @Test
    public void util_test() {
        String script = "import '/ruler/std/util.txt' util; var arr = [2, 1, 85, 15,3]; println(util.NotContainsAnyOne(63, arr, fun(v1, v2) { return v1 == v2; }));";
        RulerRunner run = Ruler.compile(script, configuration);
        run.run();
    }

    @Test
    public void util_eq_arr_test() {
        String script = "import '/ruler/std/util.txt' util; println(util.EqArrayEveryOne([1,2, 3], [3, 2, 1]));";
        RulerRunner run = Ruler.compile(script, configuration);
        run.run();
    }

    @Test
    public void util_eq_arr_any() {
        String script = "import '/ruler/std/util.txt' util; println(util.EqArrayAnyOne([11,21, 31], [311, 211, 11]));";
        RulerRunner run = Ruler.compile(script, configuration);
        run.run();
    }

    @Test
    public void eq_null() {
        String s = "((util.EqArrayAnyOne($_start_user_key, ['125'])) && (util.Eq($var_623a8e5cfe7b9e1b639f2679, 'c8024e9451db4b37b86643c6fb8b8c71')))";
        RulerRunner run = Ruler.compile(s, configuration);

        RulerParameter p = new RulerParameter(ValueType.STRING, "var_623a8e5cfe7b9e1b639f2679",
                "9b5c863074464179bf98acf53344bf21");

        List list = new ArrayList();
        list.add(p);

        RulerResult result = run.run(list);
        System.out.println(result.first().toBoolean());
    }

    @Test
    public void time_stamp_check() {
        String script = "import '/ruler/std/sort.txt' sort; var arr = [2, 1, 85, 15,3]; sort.Sort(arr); println(arr);";
        RulerRunner run = Ruler.compile(script, configuration);
        System.out.println("- " + System.currentTimeMillis());
        run.run();
        System.out.println("- " + System.currentTimeMillis());
    }

    @Test
    public void loop_root_return() {
        String script = "var i = 0; while i < 10 { i = i + 1; if i == 5 { return 'lisicen'; } } return 'hehe';";
        RulerRunner run = Ruler.compile(script, configuration);
        Assert.assertEquals("lisicen", run.run().first().toString());
    }

    @Test
    public void fun_args_test() {
        String script = "var a = fun() { return _args_[1][1]; }; return a(1, [1, 2], 1.1);";
        RulerRunner run = Ruler.compile(script, configuration);
        Assert.assertEquals(2L, run.run().first().toInteger());
    }

    @Test
    public void global_script_test() {
        String script = "println(ok.Ok());";
        RulerRunner run = Ruler.compile(script, configuration);
        run.run();
    }

    @Test
    public void string_plus_test() {
        String script = "return 'hello' ++ ' world';";
        RulerRunner runner = Ruler.compile(script, configuration);
        Assert.assertEquals("hello world", runner.run().first().toString());
    }

    @Test
    public void scopeTest1() {
        String script = "var f = fun() { return a; }; var a = 100; return f();";
        RulerRunner run = Ruler.compile(script, configuration);
        Assert.assertEquals(100L, run.run().first().toInteger());
    }

    @Test
    public void testAssign() {
        String script = "var num = 0; for arg in [1, 2, 3] { num = num + arg; } return num;";
        RulerRunner runner = Ruler.compile(script, configuration);
        Assert.assertEquals(6L, runner.run().first().toInteger());
    }

    @Test
    public void testIf() {
        String script = "var i = 0; if true {} i = i + 1; return i;";
        RulerRunner runner = Ruler.compile(script, configuration);
        Assert.assertEquals(1L, runner.run().first().toInteger());
    }

    @Test
    public void testSimpleBreak() {
        String str = "var i = 0; while i < 10 { i = i + 1; if i == 5 { break; } } return i;";
        RulerRunner runner = Ruler.compile(str, configuration);
        Assert.assertEquals(5L, runner.run().first().toInteger());
    }

    @Test
    public void testStaticCap() {
        String str = "var a = 1; var f = fun[a]() { return a; }; a = 2; return f();";
        RulerRunner runner = Ruler.compile(str, configuration);
        Assert.assertEquals(1L, runner.run().first().toInteger());
    }

    @Test
    public void test_explicit_self() {
        String str = "var obj = { name: 'ruler', getName: fun(self) { return self.name; } }; return obj.getName();";
        RulerRunner runner = Ruler.compile(str, configuration);
        Assert.assertEquals("ruler", runner.run().first().toString());
    }

    @Test
    public void test_method_bound_self() {
        String str = "var obj = { name: 'bound', getName: fun(self, suffix) { return self.name ++ suffix; } }; var m = obj.getName; return m('!');";
        RulerRunner runner = Ruler.compile(str, configuration);
        Assert.assertEquals("bound!", runner.run().first().toString());
    }

    @Test
    public void test_method_no_self_direct_call() {
        String str = "var f = fun(self, a, b) { return a + b; }; var obj = { add: f }; return obj.add(1, 2);";
        RulerRunner runner = Ruler.compile(str, configuration);
        Assert.assertEquals(3L, runner.run().first().toInteger());
    }
}
