package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import com.kamijoucen.ruler.domain.parameter.SubRuleResultValue;
import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.service.RulerRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 模式匹配的边界与补充用例。
 * 与 MatchExpressionTest / MatchStructureTest 相互独立，集中覆盖：
 * - 负数字面量模式
 * - 跨类型严格相等的负例
 * - 空集合 / 空对象模式的双向匹配
 * - 对象模式字段值字面量不等、字段名重复校验
 * - rest 捕获的类型与空集合
 * - guard 访问函数与 binding shadow
 * - match 在 while / rule 块内
 * - 其它错误路径（空 match、顶层 rest、`-` 后非数值）
 */
public class MatchEdgeCaseTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerRunner getRunner(String text) {
        return Ruler.compile(text, configuration);
    }

    // ===== 负数字面量模式 =====

    @Test
    public void negativeIntegerLiteralPatternTest() {
        String script = "match -5 {\n    -5 -> 'neg'\n    _ -> 'other'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("neg", result.first().toString());
    }

    @Test
    public void negativeDoubleLiteralPatternTest() {
        String script = "match -3.14 {\n    -3.14 -> 'pi'\n    _ -> 'other'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("pi", result.first().toString());
    }

    @Test
    public void negativeLiteralNotMatchTest() {
        String script = "match 5 {\n    -5 -> 'neg'\n    _ -> 'pos'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("pos", result.first().toString());
    }

    @Test
    public void negativeSubToIdentifierShouldFailTest() {
        // `-x` 不是合法模式：`-` 仅对字面量有效，后面是 IDENTIFIER 应报错。
        String script = "match 0 {\n    -x -> 'bad'\n    _ -> 'ok'\n}";
        try {
            getRunner(script).run();
            Assert.fail("Expected SyntaxException for unary minus before identifier in pattern");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("unsupported pattern type"));
        }
    }

    // ===== 跨类型严格相等负例 / 补充 =====

    @Test
    public void doubleMismatchTest() {
        String script = "match 1.5 {\n    2.5 -> 'x'\n    _ -> 'y'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("y", result.first().toString());
    }

    @Test
    public void crossNumericZeroEqualityTest() {
        // 0 === 0.0 为 true（NumberUtil.compareNumbers 跨类型比较）
        String script = "match 0 {\n    0.0 -> 'eq'\n    _ -> 'ne'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("eq", result.first().toString());
    }

    @Test
    public void boolLiteralPatternOnNumberTest() {
        // 数值不会被布尔字面量匹配（BOOL 不是数值类型）
        String script = "match 1 {\n    true -> 'bad'\n    _ -> 'ok'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("ok", result.first().toString());
    }

    @Test
    public void emptyStringLiteralMatchTest() {
        String script = "match '' {\n    '' -> 'empty'\n    _ -> 'nonempty'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("empty", result.first().toString());
    }

    // ===== 空集合 / 空对象模式双向 =====

    @Test
    public void emptyObjectScrutineeEmptyPatternTest() {
        String script = "match {} {\n    {} -> 'any'\n    _ -> 'other'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("any", result.first().toString());
    }

    @Test
    public void emptyObjectScrutineeNonEmptyPatternTest() {
        // 空对象不含任何字段，带字段的模式应不匹配
        String script = "match {} {\n    {a: var x} -> 'has-a'\n    _ -> 'none'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("none", result.first().toString());
    }

    // ===== 对象字面量字段值不等 & 字段名重复校验 =====

    @Test
    public void objectLiteralFieldValueMismatchTest() {
        String script = "match {a: 1} {\n    {a: 2} -> 'x'\n    _ -> 'y'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("y", result.first().toString());
    }

    @Test
    public void objectDuplicateFieldNamePatternTest() {
        // 同一对象模式中字段名重复应在解析期报错
        String script = "match {a: 1} {\n    {a: var x, a: var y} -> 'bad'\n}";
        try {
            getRunner(script).run();
            Assert.fail("Expected SyntaxException for duplicate field in object pattern");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("duplicate field"));
        }
    }

    // ===== rest 捕获的类型与空集合 =====

    @Test
    public void arrayRestTypeIsArrayTest() {
        String script = "match [1, 2, 3] {\n    [_, ...var tail] -> typeof(tail)\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("array", result.first().toString());
    }

    @Test
    public void objectRestTypeIsObjectTest() {
        String script = "match {a: 1, b: 2} {\n    {a: _, ...var rest} -> typeof(rest)\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("object", result.first().toString());
    }

    @Test
    public void arrayRestCapturesEmptyTest() {
        String script = "match [1] {\n    [var a, ...var tail] -> tail.length()\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(0, result.first().toInteger());
    }

    @Test
    public void objectRestCapturesEmptyTest() {
        // 所有字段都被显式消费后，rest 对应空对象
        String script = "match {a: 1} {\n    {a: var x, ...var rest} -> typeof(rest)\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("object", result.first().toString());
    }

    // ===== guard / binding =====

    @Test
    public void guardCallsFunctionTest() {
        String script = "fun isPositive(x) { x > 0 }\n"
                + "match 5 {\n    var n if isPositive(n) -> 'pos'\n    _ -> 'np'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("pos", result.first().toString());
    }

    @Test
    public void bindingShadowsOuterTest() {
        // 外层 n=99；case 中 var n 绑定应 shadow 外层变量，返回 5
        String script = "var n = 99\n"
                + "match 5 {\n    var n -> n\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(5, result.first().toInteger());
    }

    @Test
    public void multipleWildcardsDoNotCollideTest() {
        // 多个 `_` 不应触发 duplicate binding（WildcardPatternNode 不产生绑定）
        String script = "match [1, 2, 3] {\n    [_, _, _] -> 'three'\n    _ -> 'other'\n}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals("three", result.first().toString());
    }

    // ===== match 在 while / rule 中 =====

    @Test
    public void matchInWhileTest() {
        String script = "var i = 0\nvar sum = 0\n"
                + "while i < 3 {\n"
                + "    sum = sum + match i {\n"
                + "        0 -> 10\n"
                + "        1 -> 20\n"
                + "        _ -> 30\n"
                + "    }\n"
                + "    i = i + 1\n"
                + "}\n"
                + "return sum";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(60, result.first().toInteger());
    }

    @Test
    public void matchInRuleBlockTest() {
        String script = "rule 'r1' {\n"
                + "    return match 2 {\n"
                + "        1 -> 'one'\n"
                + "        2 -> 'two'\n"
                + "        _ -> 'other'\n"
                + "    }\n"
                + "}";
        RulerResult result = getRunner(script).run();
        Assert.assertEquals(1, result.size());
        Object value = result.first().getValue();
        Assert.assertTrue(value instanceof SubRuleResultValue);
        SubRuleResultValue srv = (SubRuleResultValue) value;
        Assert.assertEquals("r1", srv.getName());
        Assert.assertEquals(1, srv.getValues().size());
        Assert.assertEquals("two", srv.getValues().get(0).toString());
    }

    // ===== 错误路径 =====

    @Test
    public void emptyMatchBlockSyntaxTest() {
        String script = "match 1 { }";
        try {
            getRunner(script).run();
            Assert.fail("Expected SyntaxException for empty match block");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("at least one case"));
        }
    }

    @Test
    public void restPatternAtTopLevelErrorTest() {
        // `...name` 不能单独作为 case 的顶层模式
        String script = "match [1, 2] {\n    ...r -> r\n}";
        try {
            getRunner(script).run();
            Assert.fail("Expected SyntaxException for top-level rest pattern");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("unsupported pattern type"));
        }
    }

}
