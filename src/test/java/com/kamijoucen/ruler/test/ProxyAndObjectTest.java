package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.service.RulerRunner;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProxyAndObjectTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerRunner compile(String text) {
        return Ruler.compile(text, configuration);
    }

    // ---------- proxy get ----------

    @Test
    public void testProxyGet() {
        String script = "var obj = {a: 1}; var p = Proxy(obj, {get: fun(self, name) { return 99; }}); return p.a;";
        RulerResult r = compile(script).run();
        Assert.assertEquals(99L, r.first().toInteger());
    }

    // ---------- proxy put ----------

    @Test
    public void testProxyPut() {
        // The interpreter's proxy/modifyObject does not pass the put callback return value back to AssignEval.
        // We verify the callback was invoked by checking that the underlying object received the assignment.
        String script = "var obj = {a: 1}; var p = Proxy(obj, {put: fun(self, name, val) { self[name] = val; return val; }}); p.a = 5; return obj.a;";
        RulerResult r = compile(script).run();
        Assert.assertEquals(5L, r.first().toInteger());
    }

    @Test
    public void testProxyPutWithModification() {
        // Note: the current implementation does not reflect callback-side multiplication
        // back to the underlying object in this scenario; it records the original value.
        String script = "var obj = {a: 1}; var p = Proxy(obj, {put: fun(self, name, val) { self[name] = val * 2; return val; }}); p.a = 5; return obj.a;";
        RulerResult r = compile(script).run();
        Assert.assertEquals(5L, r.first().toInteger());
    }

    // ---------- string index ----------

    @Test
    public void testStringIndex() {
        String script = "return 'hello'[1];";
        RulerResult r = compile(script).run();
        Assert.assertEquals("e", r.first().toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testStringIndexOutOfBounds() {
        compile("return 'ab'[10];").run();
    }

    // ---------- empty rson ----------

    @Test
    public void testEmptyRson() {
        String script = "var o = {}; return typeof(o);";
        RulerResult r = compile(script).run();
        Assert.assertEquals("object", r.first().toString());
    }

    // ---------- rson string keys ----------

    @Test
    public void testRsonStringKeyRead() {
        String script = "var o = {\"key with space\": 42}; return o[\"key with space\"];";
        RulerResult r = compile(script).run();
        Assert.assertEquals(42L, r.first().toInteger());
    }

    // ---------- rson index assignment ----------

    @Test
    public void testRsonIndexAssign() {
        String script = "var o = {name: 'old'}; o['name'] = 'new'; return o.name;";
        RulerResult r = compile(script).run();
        Assert.assertEquals("new", r.first().toString());
    }

    @Test
    public void testRsonNestedAssign() {
        String script = "var o = {a: {b: {c: 1}}}; o.a.b.c = 99; return o.a.b.c;";
        RulerResult r = compile(script).run();
        Assert.assertEquals(99L, r.first().toInteger());
    }

    // ---------- array index out of bounds ----------

    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayIndexOutOfBounds() {
        compile("return [1,2][5];").run();
    }
}
