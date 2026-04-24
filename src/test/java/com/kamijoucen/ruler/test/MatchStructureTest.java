package com.kamijoucen.ruler.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.service.RulerRunner;
import com.kamijoucen.ruler.domain.parameter.RulerResult;

public class MatchStructureTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerRunner getRunner(String text) {
        return Ruler.compile(text, configuration);
    }

    // ===== 数组解构 =====

    @Test
    public void arrayExactMatchTest() {
        String script = "match [1, 2, 3] {\n    [1, 2, 3] -> 'exact'\n    _ -> 'other'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("exact", result.first().toString());
    }

    @Test
    public void arrayBindingTest() {
        String script = "match [1, 2] {\n    [a, b] -> a + b\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(3, result.first().toInteger());
    }

    @Test
    public void arrayWildcardTest() {
        String script = "match [1, 2, 3] {\n    [_, x, _] -> x\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(2, result.first().toInteger());
    }

    @Test
    public void arrayEmptyMatchTest() {
        String script = "match [] {\n    [] -> 'empty'\n    _ -> 'not empty'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("empty", result.first().toString());
    }

    @Test
    public void arrayRestBindingTest() {
        String script = "match [1, 2, 3, 4] {\n    [head, ...tail] -> head + tail.length()\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(4, result.first().toInteger());
    }

    @Test
    public void arrayRestIgnoreTest() {
        String script = "match [1, 2, 3] {\n    [first, ..._] -> first\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(1, result.first().toInteger());
    }

    @Test
    public void arrayRestOnlyTest() {
        String script = "match [1, 2, 3] {\n    [...all] -> all.length()\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(3, result.first().toInteger());
    }

    @Test
    public void arrayMismatchLengthTest() {
        String script = "match [1, 2] { [a, b, c] -> 'three'; [a, b] -> 'two' }";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("two", result.first().toString());
    }

    @Test
    public void arrayMismatchTypeTest() {
        String script = "match 42 {\n    [a, b] -> 'array'\n    _ -> 'not array'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("not array", result.first().toString());
    }

    // ===== 对象解构 =====

    @Test
    public void objectExactMatchTest() {
        String script = "match {name: 'ruler'} {\n    {name: 'ruler'} -> 'matched'\n    _ -> 'other'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("matched", result.first().toString());
    }

    @Test
    public void objectBindingTest() {
        String script = "match {a: 1, b: 2} {\n    {a: x, b: y} -> x + y\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(3, result.first().toInteger());
    }

    @Test
    public void objectEmptyMatchTest() {
        String script = "match {a: 1, b: 2} {\n    {} -> 'any object'\n    _ -> 'other'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("any object", result.first().toString());
    }

    @Test
    public void objectPartialMatchTest() {
        String script = "match {name: 'ruler', age: 18} {\n    {name: 'ruler'} -> 'name ok'\n    _ -> 'other'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("name ok", result.first().toString());
    }

    @Test
    public void objectRestBindingTest() {
        String script = "match {a: 1, b: 2, c: 3} {\n    {a: x, ...rest} -> x + rest.b + rest.c\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(6, result.first().toInteger());
    }

    @Test
    public void objectRestIgnoreTest() {
        String script = "match {a: 1, b: 2} {\n    {a: x, ..._} -> x\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(1, result.first().toInteger());
    }

    @Test
    public void objectFieldMissingTest() {
        String script = "match {a: 1} {\n    {a: x, b: y} -> 'has b'\n    {a: x} -> 'only a'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("only a", result.first().toString());
    }

    @Test
    public void objectMismatchTypeTest() {
        String script = "match 42 {\n    {a: x} -> 'object'\n    _ -> 'not object'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("not object", result.first().toString());
    }

    @Test
    public void objectStringFieldNameTest() {
        String script = "match {'field-name': 42} {\n    {'field-name': v} -> v\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(42, result.first().toInteger());
    }

    // ===== 嵌套模式 =====

    @Test
    public void nestedArrayObjectTest() {
        String script = "match [{name: 'ruler'}, 2] {\n    [{name: n}, x] -> n ++ x\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("ruler2", result.first().toString());
    }

    @Test
    public void nestedObjectTest() {
        String script = "match {user: {name: 'ruler', age: 18}} {\n    {user: {name: n, age: a}} -> n ++ a\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("ruler18", result.first().toString());
    }

    @Test
    public void nestedArrayInObjectTest() {
        String script = "match {items: [1, 2, 3]} {\n    {items: [first, ..._]} -> first\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(1, result.first().toInteger());
    }

    // ===== Guard =====

    @Test
    public void guardPositiveTest() {
        String script = "match 5 {\n    n if n > 0 -> 'positive'\n    _ -> 'other'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("positive", result.first().toString());
    }

    @Test
    public void guardNegativeTest() {
        String script = "match -3 {\n    n if n > 0 -> 'positive'\n    n if n < 0 -> 'negative'\n    _ -> 'zero'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("negative", result.first().toString());
    }

    @Test
    public void guardFallbackTest() {
        String script = "match 0 {\n    n if n > 0 -> 'positive'\n    _ -> 'zero or negative'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("zero or negative", result.first().toString());
    }

    @Test
    public void guardNonBoolTest() {
        String script = "match 1 {\n    n if n + 1 -> 'bad'\n    _ -> 'ok'\n}";
        try {
            getRunner(script).run();
            Assert.fail("Expected RulerRuntimeException for non-bool guard");
        } catch (RulerRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("guard expression must return boolean"));
        }
    }

    @Test
    public void guardAccessBindingTest() {
        String script = "match [1, 2, 3] {\n    [a, b, c] if a + b == c -> 'sum ok'\n    _ -> 'other'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("sum ok", result.first().toString());
    }

    @Test
    public void guardAccessOuterScopeTest() {
        String script = "var limit = 10\nmatch 5 {\n    n if n < limit -> 'below'\n    _ -> 'above'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("below", result.first().toString());
    }

    // ===== 边界情况 =====

    @Test
    public void duplicateBindingTest() {
        String script = "match [1, 2] {\n    [a, a] -> 'dup'\n    _ -> 'other'\n}";
        try {
            getRunner(script).run();
            Assert.fail("Expected RulerRuntimeException for duplicate binding");
        } catch (RulerRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("duplicate binding"));
        }
    }

    @Test
    public void noMatchErrorTest() {
        String script = "match [1, 2] {\n    [3, 4] -> 'a'\n}";
        try {
            getRunner(script).run();
            Assert.fail("Expected RulerRuntimeException for no matching case");
        } catch (RulerRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("match error"));
        }
    }

    @Test
    public void arrayRestNotLastTest() {
        // rest 后面还有元素应该语法错误
        String script = "match [1, 2, 3] {\n    [...rest, a] -> 'bad'\n}";
        try {
            getRunner(script).run();
            Assert.fail("Expected SyntaxException for rest not at end");
        } catch (Exception e) {
            // parser 会在 ... 后 break，然后看到 a 期望 ] 但实际是逗号/a
            // 实际上由于 parseArrayPattern 在 ... 后直接 break，然后期望 ]
            // 如果后面还有 a, 那么会报 expected ']' 
            Assert.assertTrue(e.getMessage().contains("expected ']' in array pattern") ||
                              e.getMessage().contains("expected token: RIGHT_SQUARE"));
        }
    }

    @Test
    public void objectRestNotLastTest() {
        String script = "match {a: 1} {\n    {...rest, a: x} -> 'bad'\n}";
        try {
            getRunner(script).run();
            Assert.fail("Expected SyntaxException for rest not at end");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("expected '}' in object pattern") ||
                              e.getMessage().contains("expected token: RIGHT_BRACE"));
        }
    }

    @Test
    public void matchInFunctionTest() {
        String script = "fun classify(arr) { match arr { [single] -> 'single'; [a, b] -> 'pair'; _ -> 'many' } }\n" +
                        "return classify([1])";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("single", result.first().toString());
    }

    @Test
    public void matchWithReturnTest() {
        String script = "return match {status: 200, data: 'ok'} {\n" +
                        "    {status: 200, data: d} -> d\n" +
                        "    _ -> 'error'\n" +
                        "}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("ok", result.first().toString());
    }

    @Test
    public void complexNestedGuardTest() {
        String script = "match {user: {name: 'ruler', age: 25}, tags: ['a', 'b']} {\n" +
                        "    {user: {name: n, age: a}, tags: [first, ..._]} if a >= 18 -> n ++ first\n" +
                        "    _ -> 'other'\n" +
                        "}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("rulera", result.first().toString());
    }

}
