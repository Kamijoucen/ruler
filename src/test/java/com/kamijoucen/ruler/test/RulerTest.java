package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.module.RulerRunner;
import com.kamijoucen.ruler.parameter.RulerParameter;
import com.kamijoucen.ruler.parameter.RulerResult;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;
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
        configuration.registerGlobalImportPathModule("/ruler/std/global.txt", "op");
        configuration.registerGlobalImportScriptModule("var Ok = fun() { return 'OK!!!'; };", "ok");
        configuration.setMaxLoopNumber(5);
    }

    @Test
    public void test7() {

        String str = "var a = 0; if false { a = 1; b = 66;} else if true {a=2;} else { a = 3;}";

        RulerRunner runner = Ruler.compileScript(str, configuration);

        runner.run();

    }

    @Test
    public void test8() {

        String str = "var str = ''; if 5 < 5 { str = '123123'; } else { str = makeItPossible();} println(str, 1, 2, 3, '李思岑');";

        String str2 = "text = \"hello world!\"; println(text);";

        RulerRunner runner = Ruler.compileScript(str, configuration);

        runner.run();

    }

    @Test
    public void test9() {
        String str2 = "var i = 15; println(i);";
        RulerRunner runner = Ruler.compileScript(str2, configuration);

        runner.run();

    }

    @Test
    public void test_rson_parse() {

        String str = "var rson = {name:'name', age:1, doit:fun() { println('gogogogo'); },}; println(rson.name, rson.age, rson.doit());";

        RulerRunner script = Ruler.compileScript(str, configuration);

        script.run();

        System.out.println(script);

    }

    @Test
    public void test_rson_parse2() {

        String str = "var rson = {f: { ff: fun() {println('go!');} }};";

        RulerRunner script = Ruler.compileScript(str, configuration);

        System.out.println(script);

    }

    @Test
    public void test_dot_call2() {
        String str = "var test = '啊啊啊啊啊'; var name = {getAge: fun() { return 19, test; }, name: '哈哈哈哈'}; println(name.name); println(name.getAge());";

        RulerRunner script = Ruler.compileScript(str, configuration);

        script.run();
    }

    @Test
    public void test_rson_this() {

        String str = "var a = {f: fun(age) { println('------', age); return this.name; }, name: 'ggggggggg11'}; println(a.f(18));";

        RulerRunner script = Ruler.compileScript(str, configuration);

        script.run();

    }

    @Test
    public void out_name() {

        String str = "println(({a:1}).a);";

        RulerRunner script = Ruler.compileScript(str, configuration);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("a", "lisicen");
        param.put("v1", 1);
        param.put("v2", 15);

        script.run(param);

    }

    @Test
    public void typeof_test() {
        String str = "var a = '15'; println(typeof a); println(typeof (fun() {})());println(typeof 1);println(typeof 1.0);println(typeof println);";
        String sql = "var a = 5; println($a);";
        RulerRunner script = Ruler.compileScript(sql, configuration);
        script.run();
    }

    @Test
    public void null_input_test() {

        String script = "$name == '12'";
        RulerRunner runner = Ruler.compileExpression(script, configuration);

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("name", 12);

        RulerResult result = runner.run(args);

        System.out.println(result.first().toBoolean());
    }

    @Test
    public void array_call() {

        String script = "var arr = [[1]];  println(arr[0].length());";
        // String script = "var arr = [[1]]; println(arr[0]?.Length());";

        RulerRunner run = Ruler.compileScript(script, configuration);

        run.run();
    }

    @Test
    public void import_test() {

        String script = "import '/ruler/std/sort.txt' sort; var arr = [2, 1, 85, 15,3]; sort.Sort(arr); println(arr);";

        RulerRunner run = Ruler.compileScript(script, configuration);

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
            public Object call(RuntimeContext context, Environment env, BaseValue self, Object... param) {
                return "我们gg啦！！！";
            }
        });

        String text = "var a = 哈哈哈哈(); println(typeof(哈哈哈哈));";

        RulerRunner run = Ruler.compileScript(text, configuration);

        run.run();

    }

    @Test
    public void in_arr_test() {

        String script = "var arr = [2, 1, 85, 15,3]; println(listUtil.In(825, arr));";

        RulerRunner run = Ruler.compileScript(script, configuration);

        run.run();

    }

    @Test
    public void util_test() {
        String script = "import '/ruler/std/util.txt' util; var arr = [2, 1, 85, 15,3]; println(util.NotContainsAnyOne(63, arr, fun(v1, v2) { return v1 == v2; }));";

        RulerRunner run = Ruler.compileScript(script, configuration);

        run.run();
    }

    @Test
    public void util_eq_arr_test() {
        String script = "import '/ruler/std/util.txt' util; println(util.EqArrayEveryOne([1,2, 3], [3, 2, 1]));";
        RulerRunner run = Ruler.compileScript(script, configuration);
        run.run();
    }

    @Test
    public void util_eq_arr_any() {
        String script = "import '/ruler/std/util.txt' util; println(util.EqArrayAnyOne([11,21, 31], [311, 211, 11]));";
        RulerRunner run = Ruler.compileScript(script, configuration);
        run.run();
    }

    @Test
    public void eq_null() {

        String s = "((util.EqArrayAnyOne($_start_user_key, ['125'])) && (util.Eq($var_623a8e5cfe7b9e1b639f2679, 'c8024e9451db4b37b86643c6fb8b8c71')))";

        RulerRunner run = Ruler.compileExpression(s, configuration);

        RulerParameter p = new RulerParameter(ValueType.STRING, "var_623a8e5cfe7b9e1b639f2679",
                "9b5c863074464179bf98acf53344bf21");

        List list = new ArrayList();
        list.add(p);

        RulerResult result = run.run(list);
        System.out.println(result.first().toBoolean());
    }

    // @Test
    // public void loop_count_check() {
    //     String script = "var i = 0; while i < 10 { i = i + 1; println(i); }";

    //     RuleRunner run = Ruler.compileScript(script, configuration);

    //     run.run();
    // }

    @Test
    public void time_stamp_check() {
        String script = "import '/ruler/std/sort.txt' sort; var arr = [2, 1, 85, 15,3]; sort.Sort(arr); println(arr);";

        RulerRunner run = Ruler.compileScript(script, configuration);

        System.out.println("- " + System.currentTimeMillis());
        run.run();
        System.out.println("- " + System.currentTimeMillis());

    }

    @Test
    public void loop_root_return() {
        String script = "var i = 0; println((fun() {return 'aaa'; })()); while i < 10 { i = i + 1; if i == 5 { return 'lisicen'; } } return 'hehe';";
        RulerRunner run = Ruler.compileScript(script, configuration);

        RulerResult result = run.run();
        System.out.println(result);
    }

    @Test
    public void fun_args_test() {
        String script = "var a = fun() { println(_args_); }; a(1, [1, 2], 1.1);";

        RulerRunner run = Ruler.compileScript(script, configuration);
        RulerResult result = run.run();
    }

    @Test
    public void global_test() {
        String script = "println(op.Add(1, 2, -3)); println(op.Sub(6, 3, -1));";

        RulerRunner run = Ruler.compileScript(script, configuration);
        long v1 = System.currentTimeMillis();
        RulerResult result = run.run();
        long v2 = System.currentTimeMillis();
        System.out.println("time1: " + (v2 - v1));

        v1 = System.currentTimeMillis();
        run.run();
        v2 = System.currentTimeMillis();
        System.out.println("time2: " + (v2 - v1));
    }

    @Test
    public void global_script_test() {

        String script = "println(ok.Ok());";

        RulerRunner run = Ruler.compileScript(script, configuration);
        run.run();

    }

    @Test
    public void string_plus_test() {
        List<String> list = new ArrayList<String>();
        System.out.println(list.getClass().isArray());
    }


    @Test
    public void scopeTest1() {

        String script = "var f = fun() { println(a); }; var a = 100; f();";

        RulerRunner run = Ruler.compileScript(script, configuration);

        run.run();

    }

    @Test
    public void testAssign() {
        String script = "for arg in _args_:\n" +
                "        num = num + ToNumber(arg);";
        RulerRunner runner = Ruler.compileScript(script, configuration);

    }

    @Test
    public void testIf() {
        String script = "if true {} i = i + 1;";
        RulerRunner runner = Ruler.compileScript(script, configuration);
    }

    // @Test
    // public void testb() {
    //     String str = "name.test()[1].num = 15;";
    //     RuleRunner runner = Ruler.compileScript(str, configuration);
    //     System.out.println(runner.run());
    // }

    @Test
    public void testc() {

        String str = "'aaaa'.println()";

        RulerRunner runner = Ruler.compileExpression(str, configuration);

        RulerResult result = runner.run();
        System.out.println(result);
    }

    @Test
    public void testSimpleBreak() {

        String str = "var i = 0; while i < 10 { i = i + 1; if i == 5 { break; } } return i;";

        RulerRunner runner = Ruler.compileScript(str, configuration);


        Assert.assertEquals(5, runner.run().first().toInteger());

    }

    // static cap test
    @Test
    public void testStaticCap() {
        String str = "var a = 1; var f = fun[a]() { println(a); }; var b = 2; f();";
        RulerRunner runner = Ruler.compileScript(str, configuration);
        runner.run();
    }

}
