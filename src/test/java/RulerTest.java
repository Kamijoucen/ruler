import java.util.HashMap;
import java.util.Map;

import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.eval.OutNameVisitor;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.parameter.RuleResult;
import com.kamijoucen.ruler.RuleRunner;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.runtime.RuntimeContext;

import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import org.junit.Before;
import org.junit.Test;

public class RulerTest {

    public RulerConfiguration configuration;

    @Before
    public void begin() {
        configuration = new RulerConfigurationImpl();
        configuration.setGlobalImportModule("/ruler/std/collections.txt", "listUtil");
    }

    @Test
    public void test7() {

        String str = "var a = 0; if false { a = 1; b = 66;} else if true {a=2;} else { a = 3;}";

        RuleRunner runner = Ruler.compileScript(str, configuration);

        runner.run();

    }

    @Test
    public void test8() {

        String str = "var str = ''; if 5 < 5 { str = '123123'; } else { str = makeItPossible();} println(str, 1, 2, 3, '李思岑');";

        String str2 = "text = \"hello world!\"; println(text);";

        RuleRunner runner = Ruler.compileScript(str, configuration);

        runner.run();

    }

    @Test
    public void test9() {
        String str2 = "var i = 15; println(i);";
        RuleRunner runner = Ruler.compileScript(str2, configuration);

        runner.run();

    }

    @Test
    public void test_rson_parse() {

        String str = "var rson = {name:'name', age:1, doit:fun() { println('gogogogo'); },}; println(rson.name, rson.age, rson.doit());";

        RuleRunner script = Ruler.compileScript(str, configuration);

        script.run();

        System.out.println(script);

    }

    @Test
    public void test_rson_parse2() {

        String str = "var rson = {f: { ff: fun() {println('go!');} }};";

        RuleRunner script = Ruler.compileScript(str, configuration);

        System.out.println(script);

    }

    @Test
    public void test_dot_call2() {
        String str = "var test = '啊啊啊啊啊'; var name = {getAge: fun() { return 19, test; }, name: '哈哈哈哈'}; println(name.name); println(name.getAge());";

        RuleRunner script = Ruler.compileScript(str, configuration);

        script.run();
    }

    @Test
    public void test_rson_this() {

        String str = "var a = {f: fun(age) { println('------', age); return this.name; }, name: 'ggggggggg11'}; println(a.f(18));";

        RuleRunner script = Ruler.compileScript(str, configuration);

        script.run();

    }

    @Test
    public void out_name() {

        String str = "println(({a:1}).a);";

        RuleRunner script = Ruler.compileScript(str, configuration);

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
        RuleRunner script = Ruler.compileScript(sql, configuration);
        script.run();
    }

    @Test
    public void expression_test() {

        String sql = "(fun(i){return i >= 10;})(1)";

        RuleRunner script = Ruler.compileExpression(sql, configuration);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("level", 500);
        RuleResult result = script.run(param);

        System.out.println(result.first().getBoolean());

        OutNameVisitor outNameVisitor = new OutNameVisitor();
        RuntimeContext context = script.customRun(outNameVisitor);
        System.out.println(outNameVisitor.outNameTokens);
    }

    @Test
    public void str_bin_test() {
        String sql = "'500' < 500";
        RuleRunner script = Ruler.compileExpression(sql, configuration);
        RuleResult result = script.run();
        System.out.println(result.first().getBoolean());
    }

    // @Test
    public void out_name_find_test() {
        String sql = "$level < 500";
        RuleRunner script = Ruler.compileExpression(sql, configuration);
        RuleResult result = script.run();
        System.out.println(result.first().getBoolean());

        RuntimeContext context = script.customRun(new OutNameVisitor());
        System.out.println(context);
    }

    @Test
    public void null_input_test() {

        String script = "$name == '12'";
        RuleRunner runner = Ruler.compileExpression(script, configuration);

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("name", "1");

        RuleResult result = runner.run(args);

        System.out.println(result.first().getBoolean());

    }

    @Test
    public void array_call() {

        String script = "var arr = [[1]];  println(length(arr[0]));";

        RuleRunner run = Ruler.compileScript(script, configuration);

        run.run();

    }

    @Test
    public void import_test() {

        String script = "import '/ruler/std/sort.txt' sort; var arr = [2, 1, 85, 15,3]; sort.Sort(arr); println(arr);";

        RuleRunner run = Ruler.compileScript(script, configuration);

        run.run();

    }

    @Test
    public void custom_function() {

        configuration.setGlobalFunction(new RulerFunction() {
            @Override
            public String getName() {
                return "哈哈哈哈";
            }
            @Override
            public Object call(Object... param) {
                return "我们gg啦！！！";
            }
        });

        String text = "var a = 哈哈哈哈(); println(typeof(哈哈哈哈));";

        RuleRunner run = Ruler.compileScript(text, configuration);

        run.run();

    }

    @Test
    public void in_arr_test() {

        String script = "var arr = [2, 1, 85, 15,3]; println(listUtil.In(825, arr));";

        RuleRunner run = Ruler.compileScript(script, configuration);

        run.run();

    }

}
