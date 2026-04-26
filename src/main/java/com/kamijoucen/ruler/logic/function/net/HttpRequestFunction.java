package com.kamijoucen.ruler.logic.function.net;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.NullValue;
import com.kamijoucen.ruler.logic.function.RulerFunction;

import java.util.Map;

public class HttpRequestFunction implements RulerFunction {

    @Override
    public String getName() {
        return "httpRequest";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length < 2) {
            throw new RulerRuntimeException("httpRequest requires at least 2 arguments: method, url");
        }
        if (!(param[0] instanceof String)) {
            throw new RulerRuntimeException("httpRequest first argument (method) must be a string");
        }
        if (!(param[1] instanceof String)) {
            throw new RulerRuntimeException("httpRequest second argument (url) must be a string");
        }

        String method = (String) param[0];
        String url = (String) param[1];

        Map<String, Object> headers = null;
        if (param.length >= 3 && param[2] != null) {
            if (param[2] instanceof Map) {
                headers = (Map<String, Object>) param[2];
            } else if (!(param[2] instanceof NullValue)) {
                throw new RulerRuntimeException("httpRequest third argument (headers) must be an object or null");
            }
        }

        String body = null;
        if (param.length >= 4 && param[3] != null) {
            if (param[3] instanceof String) {
                body = (String) param[3];
            } else if (!(param[3] instanceof NullValue)) {
                throw new RulerRuntimeException("httpRequest fourth argument (body) must be a string or null");
            }
        }

        int timeoutMs = HttpClientUtil.DEFAULT_TIMEOUT_MS;
        if (param.length >= 5 && param[4] instanceof IntegerValue) {
            timeoutMs = ((IntegerValue) param[4]).getValue().intValue();
        }

        return HttpClientUtil.request(method, url, headers, body, timeoutMs);
    }
}
