package com.kamijoucen.ruler.logic.function.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    public static final int DEFAULT_TIMEOUT_MS = 10000;

    public static Map<String, Object> request(String method, String url, Map<String, Object> headers, String body, int timeoutMs) {
        Map<String, Object> result = new HashMap<>();
        HttpURLConnection conn = null;
        try {
            URL u = URI.create(url).toURL();
            conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod(method);
            conn.setConnectTimeout(timeoutMs);
            conn.setReadTimeout(timeoutMs);
            conn.setInstanceFollowRedirects(true);

            if (headers != null) {
                for (Map.Entry<String, Object> entry : headers.entrySet()) {
                    if (entry.getKey() != null && entry.getValue() != null) {
                        conn.setRequestProperty(entry.getKey(), entry.getValue().toString());
                    }
                }
            }

            if (body != null && !body.isEmpty()) {
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body.getBytes(StandardCharsets.UTF_8));
                }
            }

            int status = conn.getResponseCode();
            result.put("status", status);

            Map<String, List<String>> headerFields = conn.getHeaderFields();
            Map<String, Object> responseHeaders = new HashMap<>();
            for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                if (entry.getKey() != null) {
                    List<String> values = entry.getValue();
                    if (values.size() == 1) {
                        responseHeaders.put(entry.getKey(), values.get(0));
                    } else if (values.size() > 1) {
                        responseHeaders.put(entry.getKey(), values);
                    }
                }
            }
            result.put("headers", responseHeaders);

            InputStream is;
            if (status >= 200 && status < 300) {
                is = conn.getInputStream();
            } else {
                is = conn.getErrorStream();
                if (is == null) {
                    is = conn.getInputStream();
                }
            }

            String responseBody = "";
            if (is != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    responseBody = sb.toString();
                }
            }
            result.put("body", responseBody);
            result.put("error", null);

        } catch (Exception e) {
            result.put("status", -1);
            result.put("headers", new HashMap<>());
            result.put("body", "");
            result.put("error", e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }
}
