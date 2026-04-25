package com.kamijoucen.ruler.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.service.RulerRunner;

public class MatchOrPatternTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerRunner getRunner(String text) {
        return Ruler.compile(text, configuration);
    }

    // ===== 基础字面量 Or =====

    @Test
    public void literalIntOrTest() {
        String script = "match 2 {\n    1 | 2 | 3 -> 'small'\n    _ -> 'other'\n}";
        Assert.assertEquals("small", getRunner(script).run().first().toString());
    }

    @Test
    public void literalStringOrTest() {
        String script = "match 'hello' {\n    'world' | 'hello' -> 'matched'\n    _ -> 'other'\n}";
        Assert.assertEquals("matched", getRunner(script).run().first().toString());
    }

    @Test
    public void literalBoolOrTest() {
        String script = "match false {\n    true | false -> 'bool'\n    _ -> 'other'\n}";
        Assert.assertEquals("bool", getRunner(script).run().first().toString());
    }

    @Test
    public void literalNullOrTest() {
        String script = "match null {\n    null | 1 -> 'ok'\n    _ -> 'other'\n}";
        Assert.assertEquals("ok", getRunner(script).run().first().toString());
    }

    @Test
    public void literalDoubleOrTest() {
        String script = "match 3.14 {\n    2.71 | 3.14 -> 'pi'\n    _ -> 'other'\n}";
        Assert.assertEquals("pi", getRunner(script).run().first().toString());
    }

    @Test
    public void literalNegativeOrTest() {
        String script = "match -3 {\n    -5 | -3 -> 'neg'\n    _ -> 'other'\n}";
        Assert.assertEquals("neg", getRunner(script).run().first().toString());
    }

    // ===== Typeof Or =====

    @Test
    public void typeofOrTest() {
        String script = "match 42 {\n    typeof 'int' | typeof 'double' -> 'number'\n    _ -> 'other'\n}";
        Assert.assertEquals("number", getRunner(script).run().first().toString());
    }

    @Test
    public void typeofOrStringTest() {
        String script = "match 'hello' {\n    typeof 'int' | typeof 'string' -> 'ok'\n    _ -> 'other'\n}";
        Assert.assertEquals("ok", getRunner(script).run().first().toString());
    }

    // ===== 变量绑定 Or =====

    @Test
    public void nameBindingOrTest() {
        String script = "match 5 {\n    var x | var y -> x + y\n}"; // 匹配 var x，y 未绑定，这里应该只返回 x
        // 注意：如果匹配 var x，bindings 只有 x=5，body 中 x+y 会报 y 未定义
        // 重新设计：同名绑定
        String script2 = "match 5 {\n    var n | var m -> n\n}";
        Assert.assertEquals(5, getRunner(script2).run().first().toInteger());
    }

    @Test
    public void sameNameBindingAcrossBranchesTest() {
        String script = "match 5 {\n    var n | var n -> n + 1\n}";
        Assert.assertEquals(6, getRunner(script).run().first().toInteger());
    }

    // ===== 通配符 Or =====

    @Test
    public void wildcardOrBindingTest() {
        String script = "match 5 {\n    _ | var x -> 'any'\n}";
        Assert.assertEquals("any", getRunner(script).run().first().toString());
    }

    // ===== 数组解构 Or =====

    @Test
    public void arrayExactMatchOrTest() {
        String script = "match [1, 2] {\n    [1, 2] | [3, 4] -> 'matched'\n    _ -> 'other'\n}";
        Assert.assertEquals("matched", getRunner(script).run().first().toString());
    }

    @Test
    public void arrayBindingOrTest() {
        String script = "match [2, 3] {\n    [1, var x] | [2, var x] -> x\n}";
        Assert.assertEquals(3, getRunner(script).run().first().toInteger());
    }

    @Test
    public void arrayEmptyOrNonEmptyTest() {
        String script = "match [] {\n    [] | [var x] -> 'ok'\n    _ -> 'other'\n}";
        Assert.assertEquals("ok", getRunner(script).run().first().toString());
    }

    @Test
    public void arrayRestOrTest() {
        String script = "match [1, 2, 3] {\n    [var h, ...var t] | [var h] -> h + t.length()\n}";
        // 第一个分支匹配成功
        Assert.assertEquals(3, getRunner(script).run().first().toInteger());
    }

    // ===== 对象解构 Or =====

    @Test
    public void objectExactMatchOrTest() {
        String script = "match {a: 1} {\n    {a: 1} | {b: 2} -> 'matched'\n    _ -> 'other'\n}";
        Assert.assertEquals("matched", getRunner(script).run().first().toString());
    }

    @Test
    public void objectPartialMatchOrTest() {
        String script = "match {name: 'ruler', age: 18} {\n    {name: 'ruler'} | {age: 20} -> 'matched'\n    _ -> 'other'\n}";
        Assert.assertEquals("matched", getRunner(script).run().first().toString());
    }

    @Test
    public void objectRestOrTest() {
        String script = "match {a: 1, b: 2} {\n    {a: var x, ...var r} | {b: var x} -> x + r.b\n}";
        // 第一个分支匹配成功
        Assert.assertEquals(3, getRunner(script).run().first().toInteger());
    }

    @Test
    public void objectDifferentShapeOrTest() {
        String script = "match {b: 2} {\n    {a: var x} | {b: var x} -> x\n}";
        Assert.assertEquals(2, getRunner(script).run().first().toInteger());
    }

    // ===== 嵌套 Or（字段值、数组元素） =====

    @Test
    public void objectFieldValueNestedOrTest() {
        String script = "match {status: 404} {\n    {status: 200 | 404} -> 'http'\n    _ -> 'other'\n}";
        Assert.assertEquals("http", getRunner(script).run().first().toString());
    }

    @Test
    public void arrayElementNestedOrTest() {
        String script = "match [2, 'ok'] {\n    [1 | 2, var x] -> x\n}";
        Assert.assertEquals("ok", getRunner(script).run().first().toString());
    }

    @Test
    public void nestedStructureOrTest() {
        String script = "match {user: {name: 'ruler'}} {\n    {user: {name: var n}} | {guest: true} -> n\n    _ -> 'other'\n}";
        Assert.assertEquals("ruler", getRunner(script).run().first().toString());
    }

    @Test
    public void deepNestedArrayOrTest() {
        String script = "match [[[2]]] {\n    [[[1]]] | [[[2]]] -> 'matched'\n    _ -> 'other'\n}";
        Assert.assertEquals("matched", getRunner(script).run().first().toString());
    }

    // ===== 裸标识符值比较 Or =====

    @Test
    public void bareIdentOrLiteralTest() {
        String script = "var TAG = 42\n" +
                       "match 42 {\n    TAG | 100 -> 'matched'\n    _ -> 'other'\n}";
        Assert.assertEquals("matched", getRunner(script).run().first().toString());
    }

    // ===== Or + Guard =====

    @Test
    public void orWithGuardSameBindingTest() {
        String script = "match 5 {\n    var x | var y if x > 0 -> 'positive'\n    _ -> 'other'\n}";
        // var x 分支匹配，guard 中 x=5 > 0，成功
        Assert.assertEquals("positive", getRunner(script).run().first().toString());
    }

    @Test
    public void orWithGuardDifferentBindingRuntimeErrorTest() {
        // var x 分支先匹配成功，guard 中访问 y（未绑定），应报运行时错误
        String script = "match -5 {\n    var x | var y if y > 0 -> 'positive'\n    _ -> 'other'\n}";
        try {
            getRunner(script).run();
            Assert.fail("Expected RulerRuntimeException for undefined variable in guard");
        } catch (RulerRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("variable not defined") ||
                           e.getMessage().contains("undefined variable"));
        }
    }

    @Test
    public void orWithGuardSecondBranchTest() {
        String script = "match -5 {\n    var x | var y if y < 0 -> 'negative'\n    _ -> 'other'\n}";
        // var x 分支匹配，但 guard 中 y 未绑定，报错
        // 重设计：确保所有分支绑定同名变量
        String script2 = "match -5 {\n    var n | var n if n < 0 -> 'negative'\n    _ -> 'other'\n}";
        Assert.assertEquals("negative", getRunner(script2).run().first().toString());
    }

    @Test
    public void orWithGuardBindingUsedInGuardTest() {
        String script = "match [1, 2, 3] {\n    [var a, var b] | [var a, var b, var c] if a + b == c -> 'sum'\n    _ -> 'other'\n}";
        // 第二个分支匹配，a=1,b=2,c=3，1+2==3 成立
        Assert.assertEquals("sum", getRunner(script).run().first().toString());
    }

    // ===== 重复绑定 =====

    @Test
    public void duplicateBindingInOrBranchTest() {
        String script = "match [1, 2] {\n    [var x, var x] | [1, 2] -> 'dup'\n    _ -> 'other'\n}";
        try {
            getRunner(script).run();
            Assert.fail("Expected RulerRuntimeException for duplicate binding");
        } catch (RulerRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("duplicate binding"));
        }
    }

    // ===== 所有分支失败回退 =====

    @Test
    public void allBranchesFailFallbackTest() {
        String script = "match 3 {\n    1 | 2 -> 'small'\n    3 -> 'three'\n    _ -> 'other'\n}";
        Assert.assertEquals("three", getRunner(script).run().first().toString());
    }

    @Test
    public void allBranchesFailNoMatchTest() {
        String script = "match 3 {\n    1 | 2 -> 'small'\n}";
        try {
            getRunner(script).run();
            Assert.fail("Expected RulerRuntimeException for no matching case");
        } catch (RulerRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("match error"));
        }
    }

    // ===== 复杂场景 =====

    @Test
    public void orInFunctionTest() {
        String script = "fun classify(x) {\n" +
                       "    match x {\n" +
                       "        1 | 2 -> 'small'\n" +
                       "        3 | 4 | 5 -> 'medium'\n" +
                       "        _ -> 'large'\n" +
                       "    }\n" +
                       "}\n" +
                       "return classify(4)";
        Assert.assertEquals("medium", getRunner(script).run().first().toString());
    }

    @Test
    public void orInLoopTest() {
        String script = "var sum = 0\n" +
                       "for i in [1, 2, 3, 4] {\n" +
                       "    var val = match i {\n" +
                       "        1 | 2 -> 10\n" +
                       "        3 | 4 -> 20\n" +
                       "        _ -> 0\n" +
                       "    }\n" +
                       "    sum = sum + val\n" +
                       "}\n" +
                       "return sum";
        Assert.assertEquals(60, getRunner(script).run().first().toInteger());
    }

    @Test
    public void orInExpressionStatementTest() {
        String script = "match 1 {\n    1 | 2 -> 'ok'\n    _ -> 'fail'\n}\nreturn 'done'";
        Assert.assertEquals("done", getRunner(script).run().first().toString());
    }

    @Test
    public void orAsFunctionArgTest() {
        String script = "fun greet(msg) { return 'Hello, ' ++ msg }\n" +
                       "return greet(match 1 {\n    1 | 2 -> 'World'\n    _ -> 'Unknown'\n})";
        Assert.assertEquals("Hello, World", getRunner(script).run().first().toString());
    }

    // ===== | 在普通表达式中报错 =====

    @Test
    public void pipeInRegularExpressionErrorTest() {
        String script = "var x = 1 | 2";
        try {
            getRunner(script).run();
            Assert.fail("Expected SyntaxException for | in regular expression");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("only allowed in pattern matching"));
        }
    }

    @Test
    public void pipeInIfConditionErrorTest() {
        String script = "if (true | false) { return 'ok' }";
        try {
            getRunner(script).run();
            Assert.fail("Expected SyntaxException for | in regular expression");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("only allowed in pattern matching"));
        }
    }

    @Test
    public void pipeInBinaryOpErrorTest() {
        String script = "var x = 1 + 2 | 3";
        try {
            getRunner(script).run();
            Assert.fail("Expected SyntaxException for | in regular expression");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("only allowed in pattern matching"));
        }
    }

    @Test
    public void pipeInArrayLiteralErrorTest() {
        String script = "var arr = [1 | 2]";
        try {
            getRunner(script).run();
            Assert.fail("Expected SyntaxException for | in regular expression");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("only allowed in pattern matching"));
        }
    }

    @Test
    public void pipeInObjectLiteralErrorTest() {
        String script = "var obj = {a: 1 | 2}";
        try {
            getRunner(script).run();
            Assert.fail("Expected SyntaxException for | in regular expression");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("only allowed in pattern matching"));
        }
    }

    @Test
    public void pipeInGuardExpressionErrorTest() {
        // guard 中也不能用 |
        String script = "match 1 {\n    var n if n | 2 -> 'bad'\n    _ -> 'ok'\n}";
        try {
            getRunner(script).run();
            Assert.fail("Expected SyntaxException for | in guard expression");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("only allowed in pattern matching"));
        }
    }

    // ===== 综合测试 =====

    @Test
    public void complexMixedOrPatternTest() {
        String script = "match {status: 200, data: 'ok'} {\n" +
                       "    {status: 404 | 500} -> 'error'\n" +
                       "    {status: 200 | 201, data: var d} -> d\n" +
                       "    _ -> 'unknown'\n" +
                       "}";
        Assert.assertEquals("ok", getRunner(script).run().first().toString());
    }

    @Test
    public void orWithMultipleTypeofAndLiteralTest() {
        String script = "match 3.14 {\n" +
                       "    typeof 'int' | typeof 'double' | 100 -> 'number'\n" +
                       "    _ -> 'other'\n" +
                       "}";
        Assert.assertEquals("number", getRunner(script).run().first().toString());
    }

    @Test
    public void stringFieldNameNestedOrTest() {
        String script = "match {'field-name': 42} {\n" +
                       "    {'field-name': 41 | 42} -> 'matched'\n" +
                       "    _ -> 'other'\n" +
                       "}";
        Assert.assertEquals("matched", getRunner(script).run().first().toString());
    }

    @Test
    public void restInOrBranchTest() {
        String script = "match [1, 2, 3, 4] {\n" +
                       "    [var h, ...var t] | [var h] -> h + t.length()\n" +
                       "    _ -> 'other'\n" +
                       "}";
        Assert.assertEquals(4, getRunner(script).run().first().toInteger());
    }

    @Test
    public void objectRestInOrBranchTest() {
        String script = "match {a: 1, b: 2, c: 3} {\n" +
                       "    {a: var x, ...var r} | {d: var x} -> x + r.b + r.c\n" +
                       "    _ -> 'other'\n" +
                       "}";
        Assert.assertEquals(6, getRunner(script).run().first().toInteger());
    }

}
