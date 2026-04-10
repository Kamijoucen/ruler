package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.module.RulerRunner;
import com.kamijoucen.ruler.parameter.RulerResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class ValueConversionTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerRunner compileScript(String text) {
        return Ruler.compileScript(text, configuration);
    }

    // ---------- Java List to Array conversion ----------

    @Test
    public void testJavaListAsParameter() {
        String script = "return $arr.length();";
        Map<String, Object> param = new HashMap<>();
        param.put("arr", Arrays.asList(1, 2, 3, 4));
        RulerResult r = compileScript(script).run(param);
        Assert.assertEquals(4L, r.first().toInteger());
    }

    @Test
    public void testJavaListIndexedAccess() {
        String script = "return $arr[1];";
        Map<String, Object> param = new HashMap<>();
        param.put("arr", Arrays.asList("a", "b", "c"));
        RulerResult r = compileScript(script).run(param);
        Assert.assertEquals("b", r.first().toString());
    }

    // ---------- Java null parameter ----------

    @Test
    public void testJavaNullParameter() {
        String script = "return $val === null;";
        Map<String, Object> param = new HashMap<>();
        param.put("val", null);
        RulerResult r = compileScript(script).run(param);
        Assert.assertTrue(r.first().toBoolean());
    }

    // ---------- Java Date parameter ----------

    @Test
    public void testJavaDatePassThrough() {
        String script = "return $d;";
        Map<String, Object> param = new HashMap<>();
        Date now = new Date();
        param.put("d", now);
        RulerResult r = compileScript(script).run(param);
        Assert.assertTrue(r.first().getValue() instanceof Date);
    }

    // ---------- Java Map to RSON conversion ----------

    @Test
    public void testJavaMapAsParameter() {
        String script = "return $obj.name;";
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> inner = new HashMap<>();
        inner.put("name", "ruler");
        param.put("obj", inner);
        RulerResult r = compileScript(script).run(param);
        Assert.assertEquals("ruler", r.first().toString());
    }

    // ---------- typeof assertions ----------

    @Test
    public void testTypeofInteger() {
        RulerResult r = compileScript("return typeof(1);").run();
        Assert.assertEquals("int", r.first().toString());
    }

    @Test
    public void testTypeofDouble() {
        RulerResult r = compileScript("return typeof(1.0);").run();
        Assert.assertEquals("double", r.first().toString());
    }

    @Test
    public void testTypeofString() {
        RulerResult r = compileScript("return typeof('hello');").run();
        Assert.assertEquals("string", r.first().toString());
    }

    @Test
    public void testTypeofBoolean() {
        RulerResult r = compileScript("return typeof(true);").run();
        Assert.assertEquals("boolean", r.first().toString());
    }

    @Test
    public void testTypeofArray() {
        RulerResult r = compileScript("return typeof([1, 2]);").run();
        Assert.assertEquals("array", r.first().toString());
    }

    @Test
    public void testTypeofObject() {
        RulerResult r = compileScript("return typeof({});").run();
        Assert.assertEquals("object", r.first().toString());
    }

    @Test
    public void testTypeofNull() {
        RulerResult r = compileScript("return typeof(null);").run();
        Assert.assertEquals("null", r.first().toString());
    }

    @Test
    public void testTypeofFunction() {
        RulerResult r = compileScript("return typeof(fun() {});").run();
        Assert.assertEquals("function", r.first().toString());
    }

    // ---------- return RSON/Array to Java ----------

    @Test
    public void testReturnArrayToJava() {
        RulerResult r = compileScript("return [1, 2, 3];").run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof List);
        List<?> list = (List<?>) val;
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void testReturnRsonToJava() {
        RulerResult r = compileScript("return {a: 1, b: 'hi'};").run();
        Object val = r.first().getValue();
        Assert.assertTrue(val instanceof Map);
        Map<?, ?> map = (Map<?, ?>) val;
        Assert.assertEquals(1L, map.get("a"));
        Assert.assertEquals("hi", map.get("b"));
    }
}
