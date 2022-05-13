package com.kamijoucen.ruler;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.compiler.impl.RulerInterpreter;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.parameter.RuleResult;
import com.kamijoucen.ruler.parameter.RuleResultValue;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.parameter.RulerParameter;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.AssertUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RuleRunner {

    private RulerModule module;
    private boolean isScript;
    private RulerConfiguration configuration;

    public RuleRunner(RulerModule module, boolean isScript, RulerConfiguration configuration) {
        this.module = module;
        this.isScript = isScript;
        this.configuration = configuration;
    }

    public RuleResult run() {
        return run((Map<String, Object>) null);
    }

    public RuleResult run(List<RulerParameter> param) {

        List<Object> values = null;
        RulerInterpreter interpreter = new RulerInterpreter(module, configuration);
        if (isScript) {
            values = interpreter.runScript(param, new Scope("runtime root", configuration.getGlobalScope()));
        } else {
            values = interpreter.runExpression(param, new Scope("runtime root", configuration.getGlobalScope()));
        }

        List<RuleResultValue> ruleResultValues = new ArrayList<RuleResultValue>(values.size());
        for (Object value : values) {
            ruleResultValues.add(new RuleResultValue(value));
        }
        return new RuleResult(ruleResultValues);
    }

    public RuleResult run(Map<String, Object> param) {
        if (param == null) {
            param = Collections.emptyMap();
        }
        List<RulerParameter> parameter = configuration.getParamTypePreProcess().process(param);
        return run(parameter);
    }

    public RuntimeContext customRun(NodeVisitor visitor) {
        AssertUtil.notNull(visitor);
        RulerInterpreter interpreter = new RulerInterpreter(module, configuration);
        return interpreter.runCustomVisitor(visitor, new Scope("runtime root", configuration.getGlobalScope()));
    }

}
