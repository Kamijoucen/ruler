package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.service.RulerRunner;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import org.junit.Assert;
import org.junit.Test;

public class ProxyEdgeCaseTest {

    @Test
    public void nestedProxyTest() {
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
        RulerRunner runner = Ruler.compileScript(script, configuration);
        RulerResult result = runner.run();
        Assert.assertEquals(11, result.first().toInteger());
    }

    @Test
    public void proxyUndefinedPutTrapArrayFallbackTest() {
        String script =
                "var arr = [1, 2, 3];" +
                "arr = Proxy(arr, {get: fun(self, name) { return self[name]; }});" +
                "arr[0] = 99;" +
                "return arr[0];";
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compileScript(script, configuration);
        try {
            runner.run();
            Assert.fail("Expected RulerRuntimeException for array index assignment on proxy treated as RSON");
        } catch (com.kamijoucen.ruler.domain.exception.RulerRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("Object key must be a string"));
        }
    }

    @Test
    public void proxyPutTrapIgnoredReturnTest() {
        // Current behavior: reading from a proxy without get trap throws UnsupportedOperationException.
        // We read the underlying object directly to verify the put callback was invoked.
        String script =
                "var obj = {a: 1};" +
                "var p = Proxy(obj, {" +
                "    put: fun(self, name, newValue) {" +
                "        self[name] = newValue + 10;" +
                "    }" +
                "});" +
                "p.a = 5;" +
                "return obj.a;";
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compileScript(script, configuration);
        RulerResult result = runner.run();
        Assert.assertEquals(15, result.first().toInteger());
    }
}
