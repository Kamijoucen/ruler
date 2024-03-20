package com.kamijoucen.ruler.config.option;


import com.kamijoucen.ruler.function.RulerFunction;

import java.util.List;

public class ConfigModule {

    private final String uri;

    private final String script;

    private final boolean isVirtualPath;

    private final List<RulerFunction> functions;

    private ConfigModule(String uri, String script, boolean isVirtualPath, List<RulerFunction> functions) {
        this.uri = uri;
        this.script = script;
        this.isVirtualPath = isVirtualPath;
        this.functions = functions;
    }

    public static ConfigModule createPathModule(String uri, String script) {
        return new ConfigModule(uri, script, false, null);
    }

    public static ConfigModule createScriptModule(String uri, String script) {
        return new ConfigModule(uri, script, true, null);
    }

    public static ConfigModule createFunctionModule(String uri, List<RulerFunction> functions) {
        return new ConfigModule(uri, null, false, functions);
    }

    public String getUri() {
        return uri;
    }

    public String getScript() {
        return script;
    }

    public boolean isVirtualPath() {
        return isVirtualPath;
    }

    public List<RulerFunction> getFunctions() {
        return functions;
    }

    public boolean isVirtualModule() {
        return isVirtualPath;
    }

    public boolean isFunctionModule() {
        return functions != null;
    }

    public boolean isPathModule() {
        return !this.isVirtualPath;
    }
}
