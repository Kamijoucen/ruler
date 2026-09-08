package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.api.Ruler;
import com.kamijoucen.ruler.api.RulerRunner;
import com.kamijoucen.ruler.types.parameter.RulerResult;
import com.kamijoucen.ruler.types.exception.IllegalOperationException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateOperationTest {

    @Test
    public void dateEqualityTest() {
        RulerConfiguration configuration = new RulerConfiguration();
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
        RulerConfiguration configuration = new RulerConfiguration();
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
        RulerConfiguration configuration = new RulerConfiguration();
        RulerRunner runner = Ruler.compile("$d1 > $d2", configuration);
        Map<String, Object> param = new HashMap<>();
        param.put("d1", new Date(1609459200000L));
        param.put("d2", new Date(1609459200001L));
        runner.run(param);
    }

    @Test
    public void dateParameterPassingAndReturnTest() {
        RulerConfiguration configuration = new RulerConfiguration();
        Date d = new Date(1609459200000L);
        RulerRunner runner = Ruler.compile("$d", configuration);
        Map<String, Object> param = new HashMap<>();
        param.put("d", d);
        RulerResult result = runner.run(param);
        Assert.assertTrue(result.first().getValue() instanceof Date);
        Assert.assertEquals(d, result.first().getValue());
    }
}
