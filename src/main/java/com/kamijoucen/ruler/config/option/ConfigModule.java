package com.kamijoucen.ruler.config.option;


import com.kamijoucen.ruler.function.RulerFunction;

import java.util.List;

public class ConfigModule {

    private final String globeAlias;

    private final String uri;

    private final String script;

    private final List<RulerFunction> functions;

    private ConfigModule(String uri, String script, List<RulerFunction> functions, String globeAlias) {
        this.uri = uri;
        this.script = script;
        this.functions = functions;
        this.globeAlias = globeAlias;
    }

    public static ConfigModule createPathModule(String uri) {
        return new ConfigModule(uri, null, null, null);
    }

    public static ConfigModule createScriptModule(String uri, String script) {
        return new ConfigModule(uri, script, null, null);
    }

    public static ConfigModule createFunctionModule(String uri, List<RulerFunction> functions) {
        return new ConfigModule(uri, null, functions, null);
    }

    public static ConfigModule createScriptModuleWithGlobeImport(String uri, String script, String globeAlias) {
        return new ConfigModule(uri, script, null, globeAlias);
    }

    public static ConfigModule createPathModuleWithGlobeImport(String uri, String globeAlias) {
        return new ConfigModule(uri, null, null, globeAlias);
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

    public boolean isGlobeImport() {
        return globeAlias != null;
    }

}
