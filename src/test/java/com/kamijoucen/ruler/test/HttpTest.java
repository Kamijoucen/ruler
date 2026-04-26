package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.RsonValue;
import com.kamijoucen.ruler.domain.value.StringValue;
import com.kamijoucen.ruler.logic.function.RulerFunction;
import com.kamijoucen.ruler.service.Ruler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerResult run(String text) {
        return Ruler.compile(text, configuration).run();
    }

    @SuppressWarnings("unchecked")
    private List<Object> list(RulerResult r) {
        return (List<Object>) r.first().getValue();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> map(RulerResult r) {
        return (Map<String, Object>) r.first().getValue();
    }

    // ==================== primitive function tests (real HTTP, optional) ====================

    @Test
    public void httpRequestGetRealTest() {
        String script = "var resp = httpRequest('GET', 'https://httpbin.org/get', null, null); return resp.status;";
        try {
            RulerResult r = run(script);
            Assert.assertEquals(200L, r.first().toInteger());
        } catch (Exception e) {
            org.junit.Assume.assumeNoException("Skipping real HTTP test (network unavailable)", e);
        }
    }

    @Test
    public void httpSendPostRealTest() {
        String script = "var resp = httpSend({method: 'POST', url: 'https://httpbin.org/post', headers: {'Content-Type': 'text/plain'}, body: 'hello'}); return resp.status;";
        try {
            RulerResult r = run(script);
            Assert.assertEquals(200L, r.first().toInteger());
        } catch (Exception e) {
            org.junit.Assume.assumeNoException("Skipping real HTTP test (network unavailable)", e);
        }
    }

    // ==================== mock primitive + script module tests ====================

    /**
     * Mock httpRequest that returns predefined responses based on URL.
     */
    private static class MockHttpRequest implements RulerFunction {
        @Override
        public String getName() {
            return "httpRequest";
        }

        @Override
        public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
            String method = (String) param[0];
            String url = (String) param[1];
            Map<String, Object> headers = param.length > 2 && param[2] instanceof Map ? (Map<String, Object>) param[2] : null;
            String body = param.length > 3 && param[3] instanceof String ? (String) param[3] : null;
            return createResponse(method, url, headers, body);
        }

        static Map<String, Object> createResponse(String method, String url, Map<String, Object> headers, String body) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", 200);
            result.put("headers", new HashMap<String, Object>());
            result.put("error", null);

            if (url.contains("/users/1")) {
                result.put("body", "{\"id\":1,\"name\":\"alice\"}");
            } else if (url.contains("/users")) {
                result.put("body", "{\"id\":2,\"name\":\"bob\"}");
            } else if (url.contains("/patch")) {
                result.put("body", "{\"patched\":true}");
            } else {
                result.put("body", "{\"success\":true,\"method\":\"" + method + "\"}");
            }
            return result;
        }
    }

    private RulerConfigurationImpl createMockConfig() {
        RulerConfigurationImpl cfg = new RulerConfigurationImpl();
        cfg.removeGlobalFunction("httpRequest");
        cfg.removeGlobalFunction("httpSend");
        cfg.registerGlobalFunction(new MockHttpRequest());
        cfg.registerGlobalFunction(new MockHttpSend());
        return cfg;
    }

    /**
     * Mock httpSend that delegates to the same logic.
     */
    private static class MockHttpSend implements RulerFunction {
        @Override
        public String getName() {
            return "httpSend";
        }

        @Override
        public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
            Map<String, Object> options = (Map<String, Object>) param[0];
            String method = (String) options.get("method");
            String url = (String) options.get("url");
            Map<String, Object> headers = options.get("headers") instanceof Map ? (Map<String, Object>) options.get("headers") : null;
            String body = options.get("body") instanceof String ? (String) options.get("body") : null;
            return MockHttpRequest.createResponse(method, url, headers, body);
        }
    }

    @Test
    public void httpModuleMockGetTest() {
        RulerConfigurationImpl cfg = createMockConfig();
        String script = "import '/ruler/std/http.txt' http; var resp = http.get('https://api.example.com/users/1', null); return resp.body;";
        RulerResult r = Ruler.compile(script, cfg).run();
        Assert.assertEquals("{\"id\":1,\"name\":\"alice\"}", r.first().toString());
    }

    @Test
    public void httpModuleMockPostTest() {
        RulerConfigurationImpl cfg = createMockConfig();
        String script = "import '/ruler/std/http.txt' http; var resp = http.post('https://api.example.com/users', {'Content-Type': 'application/json'}, '{\"name\":\"bob\"}'); return resp.body;";
        RulerResult r = Ruler.compile(script, cfg).run();
        Assert.assertEquals("{\"id\":2,\"name\":\"bob\"}", r.first().toString());
    }

    @Test
    public void httpModuleMockRequestTest() {
        RulerConfigurationImpl cfg = createMockConfig();
        String script = "import '/ruler/std/http.txt' http; var resp = http.request({method: 'DELETE', url: 'https://api.example.com/items/1'}); return resp.body;";
        RulerResult r = Ruler.compile(script, cfg).run();
        String body = r.first().toString();
        Assert.assertTrue(body.contains("\"method\":\"DELETE\""));
    }

    @Test
    public void httpModuleMockRequestJsonTest() {
        RulerConfigurationImpl cfg = createMockConfig();
        String script = "import '/ruler/std/http.txt' http; var resp = http.requestJson({method: 'PATCH', url: 'https://api.example.com/patch', body: {name: 'new', age: 20}}); return resp.data;";
        RulerResult r = Ruler.compile(script, cfg).run();
        Map<String, Object> data = map(r);
        Assert.assertTrue((Boolean) data.get("patched"));
    }

    @Test
    public void httpModuleMockRequestJsonPreservesOtherFieldsTest() {
        RulerConfigurationImpl cfg = createMockConfig();
        String script = "import '/ruler/std/http.txt' http; var resp = http.requestJson({method: 'GET', url: 'https://api.example.com/users/1', headers: {'X-Custom': 'val'}}); return resp.status;";
        RulerResult r = Ruler.compile(script, cfg).run();
        Assert.assertEquals(200L, r.first().toInteger());
    }

    @Test
    public void httpPrimitiveRequestErrorTest() {
        String script = "var resp = httpRequest('GET', 'http://localhost:59999/invalid', null, null, 500); return resp.error;";
        RulerResult r = run(script);
        Assert.assertNotNull(r.first().getValue());
    }

    @Test
    public void httpPrimitiveSendErrorTest() {
        String script = "var resp = httpSend({method: 'GET', url: 'http://localhost:59999/invalid', timeout: 500}); return resp.error;";
        RulerResult r = run(script);
        Assert.assertNotNull(r.first().getValue());
    }
}
