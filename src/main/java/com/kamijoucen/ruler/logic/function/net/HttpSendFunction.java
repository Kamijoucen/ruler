package com.kamijoucen.ruler.logic.function.net;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.NullValue;
import com.kamijoucen.ruler.logic.function.RulerFunction;

import java.util.Map;

public class HttpSendFunction implements RulerFunction {

    @Override
    public String getName() {
        return "httpSend";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0 || param[0] == null) {
            throw new RulerRuntimeException("httpSend requires an options object argument");
        }
        if (!(param[0] instanceof Map)) {
            throw new RulerRuntimeException("httpSend argument must be an object");
        }

        Map<String, Object> options = (Map<String, Object>) param[0];

        Object methodObj = options.get("method");
        Object urlObj = options.get("url");

        if (!(methodObj instanceof String)) {
            throw new RulerRuntimeException("httpSend options.method must be a string");
        }
        if (!(urlObj instanceof String)) {
            throw new RulerRuntimeException("httpSend options.url must be a string");
        }

        String method = (String) methodObj;
        String url = (String) urlObj;

        Map<String, Object> headers = null;
        Object headersObj = options.get("headers");
        if (headersObj instanceof Map) {
            headers = (Map<String, Object>) headersObj;
        } else if (headersObj != null && !(headersObj instanceof NullValue)) {
            throw new RulerRuntimeException("httpSend options.headers must be an object or null");
        }

        String body = null;
        Object bodyObj = options.get("body");
        if (bodyObj instanceof String) {
            body = (String) bodyObj;
        } else if (bodyObj != null && !(bodyObj instanceof NullValue)) {
            throw new RulerRuntimeException("httpSend options.body must be a string or null");
        }

        int timeoutMs = HttpClientUtil.DEFAULT_TIMEOUT_MS;
        Object timeoutObj = options.get("timeout");
        if (timeoutObj instanceof IntegerValue) {
            timeoutMs = ((IntegerValue) timeoutObj).getValue().intValue();
        } else if (timeoutObj instanceof Integer) {
            timeoutMs = (Integer) timeoutObj;
        }

        return HttpClientUtil.request(method, url, headers, body, timeoutMs);
    }
}
