package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.exception.PanicException;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import com.kamijoucen.ruler.service.Ruler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

public class BuiltinFunctionTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerResult run(String text) {
        return Ruler.compile(text, configuration).run();
    }

    @Test
    public void toNumberIntegerStringTest() {
        Assert.assertEquals(12L, run("return ToNumber('12');").first().toInteger());
    }

    @Test
    public void toNumberDoubleStringTest() {
        Assert.assertEquals(12.5, run("return ToNumber('12.5');").first().toDouble(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toNumberInvalidStringTest() {
        run("return ToNumber('abc');");
    }

    @Test
    public void toBooleanCaseInsensitiveStringTest() {
        Assert.assertTrue(run("return ToBoolean('TrUe');").first().toBoolean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void toBooleanInvalidValueTest() {
        run("return ToBoolean(1);");
    }

    @Ignore("Known issue: StringCharAt returns char without a registered converter")
    @Test
    public void stringCharAtTest() {
        Assert.assertEquals("u", run("return StringCharAt('ruler', 1);").first().toString());
    }

    @Ignore("Known issue: StringCharAt returns char without a registered converter")
    @Test(expected = IndexOutOfBoundsException.class)
    public void stringCharAtOutOfBoundsTest() {
        run("return StringCharAt('ab', 3);");
    }

    @Test
    public void datetimeDefaultPatternTest() {
        Object value = run("return Datetime('2024-01-02 03:04:05');").first().getValue();
        Assert.assertTrue(value instanceof Date);
    }

    @Test
    public void datetimeCustomPatternTest() {
        Object value = run("return Datetime('2024/01/02', 'yyyy/MM/dd');").first().getValue();
        Assert.assertTrue(value instanceof Date);
    }

    @Test
    public void datetimeInvalidTextReturnsNullTest() {
        Assert.assertNull(run("return Datetime('not-a-date');").first().getValue());
    }

    @Test
    public void timestampReturnsCurrentMillisTest() {
        long before = System.currentTimeMillis();
        long value = run("return Timestamp();").first().toInteger();
        long after = System.currentTimeMillis();
        Assert.assertTrue(value >= before);
        Assert.assertTrue(value <= after);
    }

    @Test
    public void callFindsNamedFunctionTest() {
        Assert.assertEquals(6L,
                run("fun add(a, b) { return a + b; } return Call('add')(2, 4);").first().toInteger());
    }

    @Test
    public void callMissingFunctionReturnsNullTest() {
        Assert.assertNull(run("return Call('missing');").first().getValue());
    }

    @Test
    public void makeItPossibleReturnsStringTest() {
        Assert.assertEquals("string", run("return typeof(makeItPossible());").first().toString());
    }

    @Test
    public void panicCarriesMessageTest() {
        try {
            run("Panic('boom');");
            Assert.fail("Expected PanicException");
        } catch (PanicException e) {
            Assert.assertEquals("boom", e.getMessage());
        }
    }
}
