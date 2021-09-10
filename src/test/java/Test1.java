import com.kamijoucen.ruler.RuleRunner;
import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.parse.DefaultLexical;
import com.kamijoucen.ruler.parse.DefaultParser;
import com.kamijoucen.ruler.parse.Lexical;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.BaseValue;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test1 {

    @Test
    public void test1() {
        String str = "n $_n 3.5 7  = == - + ** 1.1  /   '55' 1.f  /**===()-- 1. ";

        String str1 = " '11'  --";

        Lexical lexical = new DefaultLexical(str);

        Token token = lexical.nextToken();
        while (token.type != TokenType.EOF) {
            System.out.println(token);
            token = lexical.nextToken();
        }
    }

    @Test
    public void test3() {
        String str = "(a + b) * c / c";

        Lexical lexical = new DefaultLexical(str);

        lexical.nextToken();

        DefaultParser parser = new DefaultParser(lexical, null);

        BaseNode baseNode = parser.parseExpression();

        System.out.println(baseNode);

    }

    @Test
    public void test4() {

        String str = "a -- a --(-a-+b+-c)";

        Lexical lexical = new DefaultLexical(str);

        lexical.nextToken();

        DefaultParser parser = new DefaultParser(lexical, null);

        BaseNode baseNode = parser.parseExpression();

        System.out.println(baseNode);

    }
    @Test
    public void test7() {

        String str = "a = 0; if false { a = 1; b = 66;} else if true {a=2;} else { a = 3;}";

        RuleRunner runner = Ruler.compile(str);

        runner.run();

    }

    @Test
    public void test8() {

        String str = "str = ''; if 5 < 5 { str = '123123'; } else { str = makeItPossible()('1');} println(str, 1, 2, 3, '李思岑');";

        String str2 = "text = \"hello world!\"; println(text);";

        RuleRunner runner = Ruler.compile(str);

        runner.run();

    }

    @Test
    public void test9() {
        String str2 = "var i = 15; println(i);";
        RuleRunner runner = Ruler.compile(str2);

        runner.run();

    }


    @Test
    public void test_rson_parse() {

        String str = "var rson = {name:name, age:1, doit:fun() { println('gogogogo'); },}; println(rson.name, rson.age, rson.doit());";

        RuleRunner script = Ruler.compile(str);

        script.run();

        System.out.println(script);

    }

    @Test
    public void test_rson_parse2() {

        String str = "var rson = {f: { ff: fun() {println('go!');} }};";

        RuleRunner script = Ruler.compile(str);

        System.out.println(script);

    }

    @Test
    public void test_dot_call1() {
        String str = "obj.name().name[1] = 5;";

        RuleRunner script = Ruler.compile(str);

        script.run();

        System.out.println(script);

    }

    @Test
    public void test_dot_call2() {
        String str = "var test = '啊啊啊啊啊'; var name = {getAge: fun() { return 19, test; }, name: '哈哈哈哈'}; println(name.name); println(name.getAge());";

        RuleRunner script = Ruler.compile(str);

        script.run();
    }

    @Test
    public void test_rson_this() {

        String str = "var a = {f: fun(age) { println('------', age); return this.name; }, name: 'ggggggggg11'}; println(a.f(18));";

        RuleRunner script = Ruler.compile(str);

        script.run();

    }

    @Test
    public void out_name() {

        String str = "var a = 15; println($a); println(1 + 3); println($v1 + $v2);";

        RuleRunner script = Ruler.compile(str);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("a", "lisicen");
        param.put("v1", 1);
        param.put("v2", 15);

        script.run(param);

    }

    @Test
    public void import_test() {

        String content = IOUtil.read("D:\\code\\ruler\\src\\test\\java\\import_sort_test.txt");

        RuleRunner runner = Ruler.compile(content);

        runner.run();
    }

    @Test
    public void typeof_test() {
        String str = "var a = 15; println(typeof a);println(typeof 1);println(typeof 1.0);println(typeof println);";

        RuleRunner script = Ruler.compile(str);

        script.run();
    }

}
