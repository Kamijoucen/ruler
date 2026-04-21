package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.service.RulerRunner;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import org.junit.Assert;
import org.junit.Test;

/**
 * Proxy 边界情况和复杂场景测试
 */
public class ProxyEdgeCaseTest {

    @Test
    public void nestedProxyTest() {
        // 嵌套 proxy 测试（两层）
        String script =
                "var inner = {value: 10};" +
                "var innerProxy = Proxy(inner, {" +
                "    get: fun(self, name) {" +
                "        if name === 'value' { return self.value + 1; }" +
                "        return self[name];" +
                "    }" +
                "});" +
                "var outer = {p: innerProxy};" +
                "var outerProxy = Proxy(outer, {" +
                "    get: fun(self, name) {" +
                "        return self[name];" +
                "    }" +
                "});" +
                "return outerProxy.p.value;";
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile(script, configuration);
        RulerResult result = runner.run();
        Assert.assertEquals(11, result.first().toInteger());
    }

    @Test
    public void tripleNestedProxyTest() {
        // 三层 proxy 嵌套测试
        String script =
                "var obj = {value: 1};" +
                "var p1 = Proxy(obj, {get: fun(self, name) { return self[name] + 1; }});" +
                "var p2 = Proxy(p1, {get: fun(self, name) { return self[name] + 10; }});" +
                "var p3 = Proxy(p2, {get: fun(self, name) { return self[name] + 100; }});" +
                "return p3.value;";
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile(script, configuration);
        RulerResult result = runner.run();
        // obj.value (1) + 1 (p1) + 10 (p2) + 100 (p3) = 112
        Assert.assertEquals(112, result.first().toInteger());
    }

    @Test
    public void proxyArrayIndexAssignmentFallbackTest() {
        // 没有 set trap 的数组 proxy，索引赋值应该透传到数组
        String script =
                "var arr = [1, 2, 3];" +
                "arr = Proxy(arr, {get: fun(self, name) { return self[name]; }});" +
                "arr[0] = 99;" +
                "return arr[0];";
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile(script, configuration);
        RulerResult result = runner.run();
        Assert.assertEquals(99, result.first().toInteger());
    }

    @Test
    public void proxyArrayIndexWithSetTrapTest() {
        // 带 set trap 的数组 proxy，索引赋值应该被拦截
        String script =
                "var arr = [1, 2, 3];" +
                "arr = Proxy(arr, {set: fun(self, name, val) { self[name] = val * 2; return val; }});" +
                "arr[0] = 5;" +
                "return arr[0];";
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile(script, configuration);
        RulerResult result = runner.run();
        Assert.assertEquals(10, result.first().toInteger());
    }

    @Test
    public void proxySetTrapModifiesTargetTest() {
        // set trap 可以修改底层对象，obj.a 会反映这个副作用
        String script =
                "var obj = {a: 1};" +
                "var p = Proxy(obj, {" +
                "    set: fun(self, name, newValue) {" +
                "        self[name] = newValue + 10;" +
                "    }" +
                "});" +
                "p.a = 5;" +
                "return obj.a;";
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile(script, configuration);
        RulerResult result = runner.run();
        Assert.assertEquals(15, result.first().toInteger());
    }

    @Test
    public void multipleProxiesOnSameObjectTest() {
        // 多个 proxy 独立包装同一个底层对象
        String script =
                "var obj = {value: 10};" +
                "var p1 = Proxy(obj, {get: fun(self, name) { return self[name] + 1; }});" +
                "var p2 = Proxy(obj, {get: fun(self, name) { return self[name] + 100; }});" +
                "return p1.value === 11 && p2.value === 110;";
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile(script, configuration);
        RulerResult result = runner.run();
        Assert.assertTrue(result.first().toBoolean());
    }

    @Test
    public void proxyNestedTargetTest() {
        // proxy 的 target 本身也是一个 proxy
        String script =
                "var inner = {value: 1};" +
                "var innerProxy = Proxy(inner, {get: fun(self, name) { return self[name] + 10; }});" +
                "var wrapper = {inner: innerProxy};" +
                "var outerProxy = Proxy(wrapper, {get: fun(self, name) { return self[name]; }});" +
                "return outerProxy.inner.value;";
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile(script, configuration);
        RulerResult result = runner.run();
        // inner.value (1) + 10 (innerProxy) = 11
        Assert.assertEquals(11, result.first().toInteger());
    }

    @Test
    public void proxyAccessNonExistentPropertyTest() {
        // 访问不存在的属性默认返回 null（RSON 行为）
        String script =
                "var obj = {a: 1};" +
                "var p = Proxy(obj, {get: fun(self, name) { return self[name]; }});" +
                "return p.nonexistent === null;";
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile(script, configuration);
        RulerResult result = runner.run();
        Assert.assertTrue(result.first().toBoolean());
    }

    @Test
    public void proxyCustomNonExistentPropertyHandlerTest() {
        // proxy 可以自定义不存在属性的返回行为
        String script =
                "var obj = {};" +
                "var p = Proxy(obj, {get: fun(self, name) { return 'missing: ' ++ name; }});" +
                "return p.foo;";
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile(script, configuration);
        RulerResult result = runner.run();
        Assert.assertEquals("missing: foo", result.first().toString());
    }

    @Test
    public void proxyMultipleAssignmentsTest() {
        // 多次通过 proxy 对同一个属性赋值
        String script =
                "var obj = {a: 1};" +
                "var p = Proxy(obj, {set: fun(self, name, val) { self[name] = val; return val; }});" +
                "p.a = 10;" +
                "p.a = 20;" +
                "p.a = 30;" +
                "return obj.a;";
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile(script, configuration);
        RulerResult result = runner.run();
        Assert.assertEquals(30, result.first().toInteger());
    }

    @Test
    public void proxyChainedDotAccessTest() {
        // 通过 proxy 进行链式点号访问
        String script =
                "var obj = {a: {b: {c: 42}}};" +
                "var p = Proxy(obj, {get: fun(self, name) { return self[name]; }});" +
                "return p.a.b.c;";
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile(script, configuration);
        RulerResult result = runner.run();
        Assert.assertEquals(42, result.first().toInteger());
    }
}
