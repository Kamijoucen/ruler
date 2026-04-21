package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.service.RulerRunner;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Proxy 和对象相关的基础测试用例
 */
public class ProxyAndObjectTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerRunner compile(String text) {
        return Ruler.compile(text, configuration);
    }

    // ---------- proxy get 相关测试 ----------

    @Test
    public void testProxyGet() {
        String script = "var obj = {a: 1}; var p = Proxy(obj, {get: fun(self, name) { return 99; }}); return p.a;";
        RulerResult r = compile(script).run();
        Assert.assertEquals(99L, r.first().toInteger());
    }

    @Test
    public void testProxyGetWithoutSet() {
        // 只有 get trap，没有 set trap，赋值应该透传到目标对象
        String script = "var obj = {a: 1}; var p = Proxy(obj, {get: fun(self, name) { return self[name]; }}); p.a = 5; return obj.a;";
        RulerResult r = compile(script).run();
        Assert.assertEquals(5L, r.first().toInteger());
    }

    // ---------- proxy set 相关测试 ----------

    @Test
    public void testProxySet() {
        String script = "var obj = {a: 1}; var p = Proxy(obj, {set: fun(self, name, val) { self[name] = val; return val; }}); p.a = 5; return obj.a;";
        RulerResult r = compile(script).run();
        Assert.assertEquals(5L, r.first().toInteger());
    }

    @Test
    public void testProxySetWithModification() {
        // set trap 可以修改要存储的值，底层对象会反映这个修改
        String script = "var obj = {a: 1}; var p = Proxy(obj, {set: fun(self, name, val) { self[name] = val * 2; return val; }}); p.a = 5; return obj.a;";
        RulerResult r = compile(script).run();
        Assert.assertEquals(10L, r.first().toInteger());
    }

    @Test
    public void testProxySetReturnValueIsAssignmentResult() {
        // set trap 的返回值成为赋值表达式的结果（点号访问）
        String script = "var obj = {a: 1}; var p = Proxy(obj, {set: fun(self, name, val) { return val * 3; }}); var result = (p.a = 5); return result;";
        RulerResult r = compile(script).run();
        Assert.assertEquals(15L, r.first().toInteger());
    }

    @Test
    public void testProxySetIndexReturnValueIsAssignmentResult() {
        // set trap 的返回值成为赋值表达式的结果（索引访问）
        String script = "var obj = {a: 1}; var p = Proxy(obj, {set: fun(self, name, val) { return val + 10; }}); var result = (p['a'] = 5); return result;";
        RulerResult r = compile(script).run();
        Assert.assertEquals(15L, r.first().toInteger());
    }

    @Test
    public void testProxyMissingPropertyGetReturnsNull() {
        // 通过 proxy 访问不存在的属性，默认返回 null
        String script = "var obj = {}; var p = Proxy(obj, {get: fun(self, name) { return self[name]; }}); return p.nonexistent === null;";
        RulerResult r = compile(script).run();
        Assert.assertTrue(r.first().toBoolean());
    }

    // ---------- 字符串索引测试 ----------

    @Test
    public void testStringIndex() {
        String script = "return 'hello'[1];";
        RulerResult r = compile(script).run();
        Assert.assertEquals("e", r.first().toString());
    }

    @Test(expected = RulerRuntimeException.class)
    public void testStringIndexOutOfBounds() {
        compile("return 'ab'[10];").run();
    }

    // ---------- 空对象测试 ----------

    @Test
    public void testEmptyRson() {
        String script = "var o = {}; return typeof(o);";
        RulerResult r = compile(script).run();
        Assert.assertEquals("object", r.first().toString());
    }

    // ---------- 对象字符串键测试 ----------

    @Test
    public void testRsonStringKeyRead() {
        String script = "var o = {\"key with space\": 42}; return o[\"key with space\"];";
        RulerResult r = compile(script).run();
        Assert.assertEquals(42L, r.first().toInteger());
    }

    // ---------- 对象索引赋值测试 ----------

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

    // ---------- 数组越界测试 ----------

    @Test(expected = RulerRuntimeException.class)
    public void testArrayIndexOutOfBounds() {
        compile("return [1,2][5];").run();
    }
}
