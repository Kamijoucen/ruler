package com.kamijoucen.ruler.module;

import com.kamijoucen.ruler.compiler.impl.RulerInterpreter;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.parameter.RuleResultValue;
import com.kamijoucen.ruler.parameter.RulerParameter;
import com.kamijoucen.ruler.parameter.RulerResult;
import com.kamijoucen.ruler.runtime.Scope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RulerRunner implements Serializable {

    private final RulerModule module;
    private final boolean isScript;
    private transient RulerConfiguration configuration;

    public RulerRunner(RulerModule module, boolean isScript, RulerConfiguration configuration) {
        this.module = module;
        this.isScript = isScript;
        this.configuration = configuration;
    }

    public RulerResult run(List<RulerParameter> param, RulerConfiguration configuration) {
        List<Object> values;
        RulerInterpreter interpreter = new RulerInterpreter(module, configuration);
        if (isScript) {
            values = interpreter.runScript(param,
                    new Scope("runtime root", true, configuration.getGlobalScope(), null));
        } else {
            values = interpreter.runExpression(param,
                    new Scope("runtime root", true, configuration.getGlobalScope(), null));
        }

        List<RuleResultValue> ruleResultValues = new ArrayList<>(values.size());
        for (Object value : values) {
            ruleResultValues.add(new RuleResultValue(value));
        }
        return new RulerResult(ruleResultValues);
    }

    public RulerResult run() {
        return run((Map<String, Object>) null);
    }

    public RulerResult run(List<RulerParameter> param) {
        return run(param, configuration);
    }

    public RulerResult run(Map<String, Object> param) {
        if (param == null) {
            param = Collections.emptyMap();
        }
        List<RulerParameter> parameter = configuration.getParamTypePreProcess().process(param);
        return run(parameter);
    }

    public RulerConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RulerConfiguration configuration) {
        this.configuration = configuration;
    }

    public RulerModule getModule() {
        return module;
    }

    public boolean isScript() {
        return isScript;
    }

}
