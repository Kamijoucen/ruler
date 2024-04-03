package com.kamijoucen.ruler.config.option;


import com.kamijoucen.ruler.function.RulerFunction;

import java.util.List;

public class ConfigModule {

    private final String uri;

    private final String script;

    private final List<RulerFunction> functions;

    private ConfigModule(String uri, String script, List<RulerFunction> functions) {
        this.uri = uri;
        this.script = script;
        this.functions = functions;
    }

    public static ConfigModule createPathModule(String uri) {
        return new ConfigModule(uri, null, null);
    }

    public static ConfigModule createScriptModule(String uri, String script) {
        return new ConfigModule(uri, script, null);
    }

    public static ConfigModule createFunctionModule(String uri, List<RulerFunction> functions) {
        return new ConfigModule(uri, null, functions);
    }

    public String getUri() {
        return uri;
    }

    public String getScript() {
        return script;
    }

    public List<RulerFunction> getFunctions() {
        return functions;
    }

    public boolean isFunctionModule() {
        return functions != null;
    }

    public boolean isScriptModule() {
        return script != null;
    }
}
