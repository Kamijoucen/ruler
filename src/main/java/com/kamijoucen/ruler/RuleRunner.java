package com.kamijoucen.ruler;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.config.ConfigFactory;
import com.kamijoucen.ruler.config.RuntimeConfig;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerInterpreter;
import com.kamijoucen.ruler.runtime.Scope;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RuleRunner {

    private Scope scope;

    private RulerModule file;

    public RuleRunner(Scope scope, RulerModule file) {
        this.file = file;
        this.scope = scope;
    }

    public List<Object> run() {
        return run(null, false);
    }

    public List<Object> run(Map<String, Object> param) {
        return run(param, false);
    }

    public List<Object> run(Map<String, Object> param, boolean useEnv) {

        RuntimeConfig config = null;
        if (useEnv) {
            config = ConfigFactory.buildConfig(null);
        }

        if (param == null) {
            param = Collections.emptyMap();
        }
        RulerInterpreter interpreter = new RulerInterpreter(file, scope);

        return interpreter.run(param, config);
    }

}
