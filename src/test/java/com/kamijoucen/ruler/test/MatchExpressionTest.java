package com.kamijoucen.ruler.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.service.RulerRunner;
import com.kamijoucen.ruler.domain.parameter.RulerResult;

public class MatchExpressionTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerRunner getRunner(String text) {
        return Ruler.compile(text, configuration);
    }

    @Test
    public void literalIntMatchTest() {
        String script = "match 1 {\n    1 -> 'one'\n    2 -> 'two'\n    _ -> 'other'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("one", result.first().toString());
    }

    @Test
    public void literalStringMatchTest() {
        String script = "match 'hello' {\n    'hello' -> 1\n    'world' -> 2\n    _ -> 3\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(1, result.first().toInteger());
    }

    @Test
    public void literalBoolMatchTest() {
        String script = "match true {\n    true -> 'yes'\n    false -> 'no'\n    _ -> 'maybe'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("yes", result.first().toString());
    }

    @Test
    public void literalNullMatchTest() {
        String script = "match null {\n    null -> 'nothing'\n    _ -> 'something'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("nothing", result.first().toString());
    }

    @Test
    public void literalDoubleMatchTest() {
        String script = "match 3.14 {\n    3.14 -> 'pi'\n    _ -> 'other'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("pi", result.first().toString());
    }

    @Test
    public void wildcardMatchTest() {
        String script = "match 999 {\n    1 -> 'a'\n    _ -> 'b'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("b", result.first().toString());
    }

    @Test
    public void nameBindingTest() {
        String script = "match 42 {\n    n -> n + 1\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(43, result.first().toInteger());
    }

    @Test
    public void bindingScopeTest() {
        // 变量绑定不应泄漏到 match 外部
        String script = "match 1 {\n    x -> x\n}\nreturn x";
        try {
            getRunner(script).run();
            Assert.fail("Expected RulerRuntimeException for undefined variable x");
        } catch (RulerRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("variable not defined"));
        }
    }

    @Test
    public void orderTest() {
        // 匹配到第一个成功的 case 就停止
        String script = "match 1 {\n    1 -> 'first'\n    _ -> 'second'\n    1 -> 'third'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("first", result.first().toString());
    }

    @Test
    public void noMatchErrorTest() {
        String script = "match 1 {\n    2 -> 'a'\n}";
        try {
            getRunner(script).run();
            Assert.fail("Expected RulerRuntimeException for no matching case");
        } catch (RulerRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("match error"));
        }
    }

    @Test
    public void expressionContextTest() {
        String script = "var r = match 3 {\n    3 -> 'yes'\n    _ -> 'no'\n}\nreturn r";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("yes", result.first().toString());
    }

    @Test
    public void multiBindingTest() {
        String script = "match 'ok' {\n    a -> a ++ '!'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("ok!", result.first().toString());
    }

    @Test
    public void blockBodyTest() {
        String script = "match 2 {\n    1 -> 'one'\n    2 -> {\n        var x = 'two'\n        x\n    }\n    _ -> 'other'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("two", result.first().toString());
    }

    @Test
    public void strictEqualityNumberTest() {
        // 1 === 1.0 在严格相等下为 true（因为 NumberUtil.compareNumbers 认为它们相等）
        String script = "match 1 {\n    1.0 -> 'equal'\n    _ -> 'not equal'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("equal", result.first().toString());
    }

    @Test
    public void strictEqualityTypeMismatchTest() {
        // 1 === '1' 在严格相等下为 false
        String script = "match 1 {\n    '1' -> 'equal'\n    _ -> 'not equal'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("not equal", result.first().toString());
    }

    @Test
    public void nestedMatchTest() {
        String script = "match 1 {\n    1 -> match 2 {\n        2 -> 'nested ok'\n        _ -> 'nested fail'\n    }\n    _ -> 'outer fail'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("nested ok", result.first().toString());
    }

    @Test
    public void returnMatchTest() {
        String script = "return match 5 {\n    5 -> 'five'\n    _ -> 'other'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("five", result.first().toString());
    }

    @Test
    public void varAssignMatchTest() {
        String script = "var result = match 7 {\n    7 -> 'seven'\n    _ -> 'other'\n}\nreturn result";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("seven", result.first().toString());
    }

    @Test
    public void matchAsFunctionArgTest() {
        String script = "fun greet(msg) { return 'Hello, ' ++ msg }\n" +
                       "return greet(match 1 {\n    1 -> 'World'\n    _ -> 'Unknown'\n})";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("Hello, World", result.first().toString());
    }

    @Test
    public void matchInBinaryOperationTest() {
        String script = "var x = match 2 {\n    2 -> 10\n    _ -> 0\n} + match 3 {\n    3 -> 5\n    _ -> 0\n}\nreturn x";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(15, result.first().toInteger());
    }

    @Test
    public void matchInIfConditionTest() {
        String script = "if match 1 {\n    1 -> true\n    _ -> false\n} {\n    return 'matched'\n}\nreturn 'not matched'";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("matched", result.first().toString());
    }

    @Test
    public void matchInArrayTest() {
        String script = "var arr = [\n    match 1 {\n        1 -> 'one'\n        _ -> 'other'\n    },\n    match 2 {\n        2 -> 'two'\n        _ -> 'other'\n    }\n]\nreturn arr[0] ++ arr[1]";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("onetwo", result.first().toString());
    }

    @Test
    public void matchInObjectTest() {
        String script = "var obj = {\n    a: match 1 {\n        1 -> 'one'\n        _ -> 'other'\n    },\n    b: match 2 {\n        2 -> 'two'\n        _ -> 'other'\n    }\n}\nreturn obj.a ++ obj.b";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("onetwo", result.first().toString());
    }

    @Test
    public void matchInStringInterpolationTest() {
        String script = "var name = match 1 {\n    1 -> 'Ruler'\n    _ -> 'Unknown'\n}\nreturn \"Hello, {name}!\"";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("Hello, Ruler!", result.first().toString());
    }

    @Test
    public void matchAsClosureReturnTest() {
        String script = "var getValue = fun(x) -> match x {\n    1 -> 'one'\n    2 -> 'two'\n    _ -> 'many'\n}\nreturn getValue(2)";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("two", result.first().toString());
    }

    @Test
    public void multipleMatchChainedTest() {
        // 同一行内多 case 需用分号分隔
        String script = "var a = match 1 { 1 -> 10; _ -> 0 }\n" +
                       "var b = match 2 { 2 -> 20; _ -> 0 }\n" +
                       "var c = match 3 { 3 -> 30; _ -> 0 }\n" +
                       "return a + b + c";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(60, result.first().toInteger());
    }

    @Test
    public void matchInLoopTest() {
        String script = "var sum = 0\n" +
                       "for i in [1, 2, 3] {\n" +
                       "    var val = match i {\n" +
                       "        1 -> 10\n" +
                       "        2 -> 20\n" +
                       "        _ -> 30\n" +
                       "    }\n" +
                       "    sum = sum + val\n" +
                       "}\n" +
                       "return sum";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(60, result.first().toInteger());
    }

    @Test
    public void matchAsExpressionStatementTest() {
        // match 作为独立的表达式语句（虽然不常用，但语法上应支持）
        String script = "match 1 {\n    1 -> 'ok'\n    _ -> 'fail'\n}\nreturn 'done'";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("done", result.first().toString());
    }

    @Test
    public void matchWithExternalParamTest() {
        String script = "match $status {\n    200 -> 'ok'\n    404 -> 'not found'\n    _ -> 'error'\n}";
        java.util.Map<String, Object> param = new java.util.HashMap<>();
        param.put("status", 404);
        RulerResult result = getRunner(script).run(param);
        Assert.assertEquals("not found", result.first().toString());
    }

}
