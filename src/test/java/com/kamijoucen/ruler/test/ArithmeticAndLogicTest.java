package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.exception.IllegalOperationException;
import com.kamijoucen.ruler.service.RulerRunner;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArithmeticAndLogicTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerRunner compileScript(String text) {
        return Ruler.compileScript(text, configuration);
    }

    private RulerRunner compileExpression(String text) {
        return Ruler.compileExpression(text, configuration);
    }

    // ---------- arithmetic ----------

    @Test
    public void testAddInteger() {
        RulerResult r = compileExpression("1 + 2").run();
        Assert.assertEquals(3L, r.first().toInteger());
    }

    @Test
    public void testAddDouble() {
        RulerResult r = compileExpression("1.5 + 2.5").run();
        Assert.assertEquals(4.0, r.first().toDouble(), 0.0001);
    }

    @Test
    public void testAddMixed() {
        RulerResult r = compileExpression("1 + 2.5").run();
        Assert.assertEquals(3.5, r.first().toDouble(), 0.0001);
        r = compileExpression("2.5 + 1").run();
        Assert.assertEquals(3.5, r.first().toDouble(), 0.0001);
    }

    @Test
    public void testSub() {
        RulerResult r = compileExpression("10 - 3").run();
        Assert.assertEquals(7L, r.first().toInteger());
        r = compileExpression("5.5 - 2").run();
        Assert.assertEquals(3.5, r.first().toDouble(), 0.0001);
    }

    @Test
    public void testMul() {
        RulerResult r = compileExpression("3 * 4").run();
        Assert.assertEquals(12L, r.first().toInteger());
        r = compileExpression("2.5 * 2").run();
        Assert.assertEquals(5.0, r.first().toDouble(), 0.0001);
    }

    @Test
    public void testDiv() {
        RulerResult r = compileExpression("8 / 2").run();
        Assert.assertEquals(4.0, r.first().toDouble(), 0.0001);
        r = compileExpression("7 / 2").run();
        Assert.assertEquals(3.5, r.first().toDouble(), 0.0001);
    }

    @Test
    public void testDivDoubleLiteral() {
        RulerResult r = compileExpression("3.14 / 2.0").run();
        Assert.assertEquals(1.57, r.first().toDouble(), 0.0001);
    }

    @Test
    public void testUnaryMinus() {
        RulerResult r = compileExpression("-5").run();
        Assert.assertEquals(-5L, r.first().toInteger());
        r = compileExpression("-3.14").run();
        Assert.assertEquals(-3.14, r.first().toDouble(), 0.0001);
    }

    @Test
    public void testUnaryPlus() {
        RulerResult r = compileExpression("+5").run();
        Assert.assertEquals(5L, r.first().toInteger());
    }

    @Test
    public void testArithmeticPrecedence() {
        RulerResult r = compileExpression("10 - 3 * 2").run();
        Assert.assertEquals(4L, r.first().toInteger());
        r = compileExpression("(10 - 3) * 2").run();
        Assert.assertEquals(14L, r.first().toInteger());
    }

    @Test(expected = IllegalOperationException.class)
    public void testAddStringAndNumberThrows() {
        compileExpression("'a' + 1").run();
    }

    @Test(expected = IllegalOperationException.class)
    public void testBoolInArithmeticThrows() {
        compileExpression("true + 1").run();
    }

    // ---------- comparison ----------

    @Test
    public void testLessThan() {
        Assert.assertTrue(compileExpression("1 < 2").run().first().toBoolean());
        Assert.assertFalse(compileExpression("2 < 1").run().first().toBoolean());
    }

    @Test
    public void testLessThanOrEqual() {
        Assert.assertTrue(compileExpression("2 <= 2").run().first().toBoolean());
        Assert.assertTrue(compileExpression("1 <= 2").run().first().toBoolean());
        Assert.assertFalse(compileExpression("3 <= 2").run().first().toBoolean());
    }

    @Test
    public void testGreaterThan() {
        Assert.assertTrue(compileExpression("3 > 2").run().first().toBoolean());
        Assert.assertFalse(compileExpression("1 > 2").run().first().toBoolean());
    }

    @Test
    public void testGreaterThanOrEqual() {
        Assert.assertTrue(compileExpression("3 >= 3").run().first().toBoolean());
        Assert.assertTrue(compileExpression("3 >= 2").run().first().toBoolean());
        Assert.assertFalse(compileExpression("1 >= 2").run().first().toBoolean());
    }

    @Test
    public void testMixedTypeComparison() {
        Assert.assertTrue(compileExpression("1 < 1.5").run().first().toBoolean());
        Assert.assertTrue(compileExpression("2.0 > 1").run().first().toBoolean());
    }

    // ---------- equality ----------

    @Test
    public void testLooseEquality() {
        Assert.assertTrue(compileExpression("1 == 1").run().first().toBoolean());
        Assert.assertTrue(compileExpression("1 == 1.0").run().first().toBoolean());
        Assert.assertTrue(compileExpression("1 == '1'").run().first().toBoolean());
        Assert.assertFalse(compileExpression("1 == '2'").run().first().toBoolean());
        Assert.assertFalse(compileExpression("1 == 'a'").run().first().toBoolean());
        Assert.assertTrue(compileExpression("null == null").run().first().toBoolean());
        Assert.assertFalse(compileExpression("1 == null").run().first().toBoolean());
    }

    @Test
    public void testStrictEquality() {
        Assert.assertTrue(compileExpression("1 === 1").run().first().toBoolean());
        Assert.assertTrue(compileExpression("1 === 1.0").run().first().toBoolean());
        Assert.assertFalse(compileExpression("1 === '1'").run().first().toBoolean());
        Assert.assertFalse(compileExpression("1 === true").run().first().toBoolean());
        Assert.assertTrue(compileExpression("null === null").run().first().toBoolean());
        Assert.assertFalse(compileExpression("1 === null").run().first().toBoolean());
    }

    @Test
    public void testInequality() {
        Assert.assertTrue(compileExpression("1 != 2").run().first().toBoolean());
        Assert.assertFalse(compileExpression("1 != 1").run().first().toBoolean());
        Assert.assertFalse(compileExpression("1 != '1'").run().first().toBoolean());
        Assert.assertTrue(compileExpression("1 != '2'").run().first().toBoolean());
    }

    @Test
    public void testStrictInequality() {
        Assert.assertTrue(compileExpression("1 !== '1'").run().first().toBoolean());
        Assert.assertFalse(compileExpression("1 !== 1.0").run().first().toBoolean());
        Assert.assertTrue(compileExpression("1 !== true").run().first().toBoolean());
    }

    // ---------- logical ----------

    @Test
    public void testAnd() {
        Assert.assertTrue(compileExpression("true && true").run().first().toBoolean());
        Assert.assertFalse(compileExpression("true && false").run().first().toBoolean());
        Assert.assertFalse(compileExpression("false && true").run().first().toBoolean());
    }

    @Test
    public void testAndShortCircuit() {
        RulerResult r = compileScript("var x = 0; false && (x = 1); return x;").run();
        Assert.assertEquals(0L, r.first().toInteger());
    }

    @Test
    public void testOr() {
        Assert.assertTrue(compileExpression("true || false").run().first().toBoolean());
        Assert.assertFalse(compileExpression("false || false").run().first().toBoolean());
        Assert.assertTrue(compileExpression("false || true").run().first().toBoolean());
    }

    @Test
    public void testOrShortCircuit() {
        RulerResult r = compileScript("var x = 0; true || (x = 1); return x;").run();
        Assert.assertEquals(0L, r.first().toInteger());
    }

    @Test
    public void testNot() {
        Assert.assertFalse(compileExpression("!true").run().first().toBoolean());
        Assert.assertTrue(compileExpression("!false").run().first().toBoolean());
    }

    @Test(expected = IllegalOperationException.class)
    public void testNotOnNumberThrows() {
        compileExpression("!1").run();
    }

    // ---------- string concat ----------

    @Test
    public void testStringConcat() {
        RulerResult r = compileExpression("'hello' ++ ' world'").run();
        Assert.assertEquals("hello world", r.first().toString());
        r = compileScript("var a = 'aa'; var b = 'bb'; return a ++ b;").run();
        Assert.assertEquals("aabb", r.first().toString());
    }
}
