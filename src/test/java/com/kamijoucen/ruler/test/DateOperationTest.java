package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.service.RulerRunner;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import com.kamijoucen.ruler.domain.exception.IllegalOperationException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateOperationTest {

    @Test
    public void dateEqualityTest() {
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile("$d1 == $d2", configuration);
        Map<String, Object> param = new HashMap<>();
        Date d = new Date(1609459200000L);
        param.put("d1", d);
        param.put("d2", d);
        RulerResult result = runner.run(param);
        Assert.assertTrue(result.first().toBoolean());
    }

    @Test
    public void dateStrictEqualityDifferentInstanceTest() {
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile("$d1 === $d2", configuration);
        Map<String, Object> param = new HashMap<>();
        param.put("d1", new Date(1609459200000L));
        param.put("d2", new Date(1609459200000L));
        RulerResult result = runner.run(param);
        // EqOperation strict mode falls back to toString().equals() when types match and are not special-cased
        Assert.assertTrue(result.first().toBoolean());
    }

    @Test(expected = IllegalOperationException.class)
    public void dateComparisonThrowsTest() {
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile("$d1 > $d2", configuration);
        Map<String, Object> param = new HashMap<>();
        param.put("d1", new Date(1609459200000L));
        param.put("d2", new Date(1609459200001L));
        runner.run(param);
    }

    @Test
    public void dateParameterPassingAndReturnTest() {
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        Date d = new Date(1609459200000L);
        RulerRunner runner = Ruler.compile("$d", configuration);
        Map<String, Object> param = new HashMap<>();
        param.put("d", d);
        RulerResult result = runner.run(param);
        Assert.assertTrue(result.first().getValue() instanceof Date);
        Assert.assertEquals(d, result.first().getValue());
    }
}
