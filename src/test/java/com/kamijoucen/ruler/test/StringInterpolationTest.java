package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.service.RulerRunner;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class StringInterpolationTest {

    public RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
        configuration.registerGlobalImportPathModule("/ruler/std/global.txt", "op");
    }

    public RulerRunner getExpressionRunner(String text) {
        return Ruler.compile(text, configuration);
    }

    public RulerRunner getScriptRunner(String text) {
        return Ruler.compile(text, configuration);
    }

    @Test
    public void basicArithmeticInterpolation() {
        String script = "var a = 1; var b = 2; return \"a + b = {a + b}\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("a + b = 3", result.first().toString());
    }

    @Test
    public void multiPartInterpolation() {
        String script = "var name = 'Tom'; var age = 20; return \"name={name}, age={age}\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("name=Tom, age=20", result.first().toString());
    }

    @Test
    public void nestedRsonInterpolation() {
        String script = "return \"result: { {a: 1}.a }\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("result: 1", result.first().toString());
    }

    @Test
    public void functionCallInterpolation() {
        String script = "return \"val: {op.Add(1, 2, 3)}\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("val: 6", result.first().toString());
    }

    @Test
    public void prefixInterpolation() {
        String script = "var a = 1; return \"{a}bc\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("1bc", result.first().toString());
    }

    @Test
    public void suffixInterpolation() {
        String script = "var c = 2; return \"ab{c}\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("ab2", result.first().toString());
    }

    @Test
    public void onlyInterpolation() {
        String script = "var a = 42; return \"{a}\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("42", result.first().toString());
    }

    @Test
    public void blockStringInterpolation() {
        String script = "var name = 'ruler'; return \"\"\"hello\n{name}\nworld\"\"\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("hello\nruler\nworld", result.first().toString());
    }

    @Test
    public void typeAutoToString() {
        String script = "var i = 10; var d = 3.14; var b = true; var n = null; var arr = [1,2]; return \"{i},{d},{b},{n},{arr}\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("10,3.14,true,null,[1, 2]", result.first().toString());
    }

    @Test
    public void noInterpolationString() {
        String script = "return \"hello world\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("hello world", result.first().toString());
    }

    @Test
    public void singleQuoteNoInterpolation() {
        String script = "var a = 1; return 'a + b = {a}';";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("a + b = {a}", result.first().toString());
    }

    @Test
    public void escapedBrace() {
        String script = "return \"price is \\{10}\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("price is {10}", result.first().toString());
    }

    @Test
    public void escapedBackslash() {
        String script = "return \"path is \\\\home\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("path is \\home", result.first().toString());
    }

    @Test
    public void emptyString() {
        String script = "return \"\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("", result.first().toString());
    }

    @Test(expected = SyntaxException.class)
    public void unterminatedBrace() {
        String script = "return \"hello {a\";";
        getScriptRunner(script).run();
    }

    @Test(expected = SyntaxException.class)
    public void emptyInterpolation() {
        String script = "return \"hello {}\";";
        getScriptRunner(script).run();
    }

    @Test(expected = SyntaxException.class)
    public void invalidSubExpression() {
        String script = "return \"hello {a ++}\";";
        getScriptRunner(script).run();
    }

    @Test
    public void expressionRunnerInterpolation() {
        String expr = "\"hello {$name}\"";
        RulerRunner runner = getExpressionRunner(expr);
        Map<String, Object> param = new HashMap<>();
        param.put("name", "ruler");
        RulerResult result = runner.run(param);
        Assert.assertEquals("hello ruler", result.first().toString());
    }

    @Test
    public void nestedBraceInterpolation() {
        String script = "return \"result: { { k: { v: 99 } }.k.v }\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("result: 99", result.first().toString());
    }

    @Test
    public void interpolationWithStringLiteralInside() {
        String script = "return \"msg: { \\\"inner\\\" }\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("msg: inner", result.first().toString());
    }

    @Test
    public void interpolationWithSingleQuoteInside() {
        String script = "return \"msg: { 'inner' }\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("msg: inner", result.first().toString());
    }

    @Test
    public void multipleEscapes() {
        String script = "return \"a\\{b\\{c\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("a{b{c", result.first().toString());
    }

    // 以下测试覆盖"字符串里同时存在插值和转义"的场景 —— 无插值时的转义由 StringEval 处理，
    // 有插值时由 parseInterpolation 处理，必须分别覆盖

    @Test
    public void escapedBrace_withInterpolation() {
        String script = "return \"price is \\{10} and {1+1}\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("price is {10} and 2", result.first().toString());
    }

    @Test
    public void escapedBackslash_withInterpolation() {
        String script = "return \"path is \\\\home and {1+1}\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("path is \\home and 2", result.first().toString());
    }

    @Test
    public void multipleEscapes_withInterpolation() {
        String script = "return \"a\\{b\\{c and {1+1}\";";
        RulerResult result = getScriptRunner(script).run();
        Assert.assertEquals("a{b{c and 2", result.first().toString());
    }
}
