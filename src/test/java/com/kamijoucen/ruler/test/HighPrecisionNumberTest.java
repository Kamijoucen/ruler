package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.service.RulerRunner;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class HighPrecisionNumberTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerRunner compile(String text) {
        return Ruler.compile(text, configuration);
    }

    // ==================== 超大整数运算 ====================

    @Test
    public void testBigIntegerAddBeyondLong() {
        // Long.MAX_VALUE = 9223372036854775807
        String script = "return 9223372036854775807 + 1;";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigInteger);
        Assert.assertEquals(new BigInteger("9223372036854775808"), val);
    }

    @Test
    public void testBigIntegerMulBeyondLong() {
        String script = "return 999999999999999999 * 999999999999999999;";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigInteger);
        Assert.assertEquals(new BigInteger("999999999999999998000000000000000001"), val);
    }

    @Test
    public void testBigIntegerSubNegative() {
        String script = "return 0 - 9223372036854775808;";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigInteger);
        Assert.assertEquals(new BigInteger("-9223372036854775808"), val);
    }

    @Test
    public void testBigIntegerLiteral() {
        String script = "return 123456789012345678901234567890;";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigInteger);
        Assert.assertEquals(new BigInteger("123456789012345678901234567890"), val);
    }

    // ==================== 高精度浮点运算 ====================

    @Test
    public void testBigDecimalPrecision() {
        // 经典精度问题：0.1 + 0.2
        String script = "return 0.1 + 0.2;";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigDecimal);
        Assert.assertEquals(0, new BigDecimal("0.3").compareTo((BigDecimal) val));
    }

    @Test
    public void testBigDecimalDivPrecision() {
        String script = "return 1 / 3;";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigDecimal);
        BigDecimal expected = new BigDecimal("1").divide(new BigDecimal("3"), 16, java.math.RoundingMode.HALF_UP);
        Assert.assertEquals(expected, val);
    }

    @Test
    public void testBigDecimalHighPrecisionLiteral() {
        String script = "return 3.14159265358979323846;";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigDecimal);
        Assert.assertEquals(new BigDecimal("3.14159265358979323846"), val);
    }

    @Test
    public void testBigDecimalSub() {
        String script = "return 1.0 - 0.9;";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigDecimal);
        Assert.assertEquals(0, new BigDecimal("0.1").compareTo((BigDecimal) val));
    }

    // ==================== 混合运算 ====================

    @Test
    public void testMixedIntAndDouble() {
        String script = "return 5 + 2.5;";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigDecimal);
        Assert.assertEquals(0, new BigDecimal("7.5").compareTo((BigDecimal) val));
    }

    @Test
    public void testMixedBigIntAndDouble() {
        String script = "return 999999999999999999 + 0.5;";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigDecimal);
        Assert.assertEquals(0, new BigDecimal("999999999999999999.5").compareTo((BigDecimal) val));
    }

    @Test
    public void testIntDivReturnsDouble() {
        String script = "return 7 / 2;";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigDecimal);
        Assert.assertEquals(0, new BigDecimal("3.5").compareTo((BigDecimal) val));
    }

    // ==================== 比较运算 ====================

    @Test
    public void testCompareBigIntegers() {
        Assert.assertTrue(compile("9223372036854775808 > 9223372036854775807").run().first().toBoolean());
        Assert.assertFalse(compile("9223372036854775808 < 9223372036854775807").run().first().toBoolean());
        Assert.assertTrue(compile("9223372036854775808 == 9223372036854775808").run().first().toBoolean());
    }

    @Test
    public void testCompareBigDecimals() {
        Assert.assertTrue(compile("1.0000000000000001 > 1.0").run().first().toBoolean());
        Assert.assertTrue(compile("1.0 == 1.00").run().first().toBoolean());
        Assert.assertTrue(compile("1.0 === 1.00").run().first().toBoolean());
    }

    @Test
    public void testCompareMixedBigTypes() {
        Assert.assertTrue(compile("9223372036854775808 == 9223372036854775808.0").run().first().toBoolean());
        Assert.assertTrue(compile("9223372036854775808 === 9223372036854775808.0").run().first().toBoolean());
    }

    // ==================== 边界情况 ====================

    @Test
    public void testZeroOperations() {
        Assert.assertEquals(BigInteger.ZERO, compile("return 0;").run().first().getValue());
        Assert.assertEquals(BigInteger.ZERO, compile("return 1 - 1;").run().first().getValue());
        Assert.assertEquals(BigInteger.ZERO, compile("return 0 * 999999999999999999;").run().first().getValue());
    }

    @Test(expected = ArithmeticException.class)
    public void testDivByZero() {
        compile("return 1 / 0;").run();
    }

    @Test
    public void testNegativeNumbers() {
        Assert.assertEquals(new BigInteger("-42"), compile("return -42;").run().first().getValue());
        Assert.assertEquals(new BigInteger("-42"), compile("return 0 - 42;").run().first().getValue());
        Assert.assertEquals(new BigDecimal("-3.14"), compile("return -3.14;").run().first().getValue());
    }

    // ==================== Java 参数传入高精度类型 ====================

    @Test
    public void testJavaBigIntegerParameter() {
        String script = "return $big + 1;";
        Map<String, Object> param = new HashMap<>();
        param.put("big", new BigInteger("999999999999999999"));
        RulerResult r = compile(script).run(param);
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigInteger);
        Assert.assertEquals(new BigInteger("1000000000000000000"), val);
    }

    @Test
    public void testJavaBigDecimalParameter() {
        String script = "return $dec + 0.1;";
        Map<String, Object> param = new HashMap<>();
        param.put("dec", new BigDecimal("0.1"));
        RulerResult r = compile(script).run(param);
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigDecimal);
        Assert.assertEquals(0, new BigDecimal("0.2").compareTo((BigDecimal) val));
    }

    @Test
    public void testJavaLongParameterConvertedToBigInteger() {
        String script = "return $n + 1;";
        Map<String, Object> param = new HashMap<>();
        param.put("n", Long.MAX_VALUE);
        RulerResult r = compile(script).run(param);
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigInteger);
        Assert.assertEquals(new BigInteger("9223372036854775808"), val);
    }

    @Test
    public void testJavaDoubleParameterConvertedToBigDecimal() {
        String script = "return $n + 0.1;";
        Map<String, Object> param = new HashMap<>();
        param.put("n", 0.1);
        RulerResult r = compile(script).run(param);
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigDecimal);
        // double 0.1 传入后会转为 BigDecimal，注意精度
        Assert.assertTrue(val.toString().startsWith("0.2"));
    }

    // ==================== 出参类型验证 ====================

    @Test
    public void testReturnTypeIsBigInteger() {
        String script = "return 42;";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue("Expected BigInteger but got " + val.getClass().getName(),
                val instanceof BigInteger);
        Assert.assertEquals(new BigInteger("42"), val);
    }

    @Test
    public void testReturnTypeIsBigDecimal() {
        String script = "return 3.14;";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue("Expected BigDecimal but got " + val.getClass().getName(),
                val instanceof BigDecimal);
        Assert.assertEquals(new BigDecimal("3.14"), val);
    }

    @Test
    public void testReturnTypeInArray() {
        String script = "return [1, 2.5, 999999999999999999];";
        RulerResult r = compile(script).run();
        List<?> list = (List<?>) r.first().getValue();
        Assert.assertTrue(list.get(0) instanceof BigInteger);
        Assert.assertTrue(list.get(1) instanceof BigDecimal);
        Assert.assertTrue(list.get(2) instanceof BigInteger);
    }

    @Test
    public void testReturnTypeInRson() {
        String script = "return {i: 1, d: 2.5, big: 999999999999999999};";
        RulerResult r = compile(script).run();
        Map<?, ?> map = (Map<?, ?>) r.first().getValue();
        Assert.assertTrue(map.get("i") instanceof BigInteger);
        Assert.assertTrue(map.get("d") instanceof BigDecimal);
        Assert.assertTrue(map.get("big") instanceof BigInteger);
    }

    // ==================== 兼容性转换方法 ====================

    @Test
    public void testToLongCompatibility() {
        String script = "return 42;";
        RulerResult r = compile(script).run();
        Assert.assertEquals(42L, r.first().toLong());
        Assert.assertEquals(42L, r.first().toInteger());
    }

    @Test
    public void testToDoubleCompatibility() {
        String script = "return 3.14;";
        RulerResult r = compile(script).run();
        Assert.assertEquals(3.14, r.first().toDouble(), 0.0001);
    }

    @Test
    public void testToLongWithBigInteger() {
        String script = "return 999999999999999999;";
        RulerResult r = compile(script).run();
        // 999999999999999999 在 long 范围内，应该能正常转换
        Assert.assertEquals(999999999999999999L, r.first().toLong());
    }

    @Test
    public void testToLongOverflow() {
        String script = "return 9223372036854775808;";
        RulerResult r = compile(script).run();
        // 9223372036854775808 超出 long 范围，转换会溢出
        long val = r.first().toLong();
        Assert.assertTrue(val < 0); // 溢出后变为负数
    }

    // ==================== 数学函数高精度测试 ====================

    @Test
    public void testAbsWithBigInteger() {
        String script = "return abs(-9223372036854775808);";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigInteger);
        Assert.assertEquals(new BigInteger("9223372036854775808"), val);
    }

    @Test
    public void testRoundReturnsBigInteger() {
        String script = "return round(2.6);";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigInteger);
        Assert.assertEquals(new BigInteger("3"), val);
    }

    @Test
    public void testFloorReturnsBigInteger() {
        String script = "return floor(2.9);";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigInteger);
        Assert.assertEquals(new BigInteger("2"), val);
    }

    @Test
    public void testCeilReturnsBigInteger() {
        String script = "return ceil(2.1);";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigInteger);
        Assert.assertEquals(new BigInteger("3"), val);
    }

    @Test
    public void testMinWithMixedTypes() {
        String script = "return min(1, 2.5, -1);";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigDecimal);
        Assert.assertEquals(0, new BigDecimal("-1").compareTo((BigDecimal) val));
    }

    @Test
    public void testMaxWithBigInteger() {
        String script = "return max(9223372036854775808, 1, 2);";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigInteger);
        Assert.assertEquals(new BigInteger("9223372036854775808"), val);
    }

    @Test
    public void testPowReturnsBigDecimal() {
        String script = "return pow(2, 10);";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigDecimal);
        Assert.assertEquals(0, new BigDecimal("1024").compareTo((BigDecimal) val));
    }

    @Test
    public void testSqrtReturnsBigDecimal() {
        String script = "return sqrt(16);";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigDecimal);
        Assert.assertEquals(0, new BigDecimal("4").compareTo((BigDecimal) val));
    }

    // ==================== 字符串转数字 ====================

    @Test
    public void testStringToBigInteger() {
        String script = "return ToNumber('999999999999999999');";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigInteger);
        Assert.assertEquals(new BigInteger("999999999999999999"), val);
    }

    @Test
    public void testStringToBigDecimal() {
        String script = "return ToNumber('3.14159265358979323846');";
        RulerResult r = compile(script).run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof BigDecimal);
        Assert.assertEquals(new BigDecimal("3.14159265358979323846"), val);
    }

    @Test
    public void testInvalidStringToNumber() {
        String script = "return ToNumber('not-a-number');";
        RulerResult r = compile(script).run();
        Assert.assertNull(r.first().getValue());
    }
}
