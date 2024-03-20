package com.kamijoucen.ruler.config.option;


import com.kamijoucen.ruler.function.RulerFunction;

import java.util.List;

public class ConfigModule {

    private final String uri;

    private final String alias;

    private final String script;

    private final boolean isVirtualPath;

    private final List<RulerFunction> functions;

    private ConfigModule(String uri, String alias, String script, boolean isVirtualPath, List<RulerFunction> functions) {
        this.uri = uri;
        this.alias = alias;
        this.script = script;
        this.isVirtualPath = isVirtualPath;
        this.functions = functions;
    }

    public static ConfigModule createPathModule(String uri, String alias, String script) {
        return new ConfigModule(uri, alias, script, false, null);
    }

    public static ConfigModule createScriptModule(String uri, String alias, String script) {
        return new ConfigModule(uri, alias, script, true, null);
    }

    public static ConfigModule createFunctionModule(String uri, String alias, List<RulerFunction> functions) {
        return new ConfigModule(uri, alias, null, false, functions);
    }

    public String getUri() {
        return uri;
    }

    public String getAlias() {
        return alias;
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
