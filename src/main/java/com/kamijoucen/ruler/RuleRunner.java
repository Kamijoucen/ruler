package com.kamijoucen.ruler;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.compiler.RulerInterpreter;
import com.kamijoucen.ruler.runtime.ImportCache;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.result.RuleResult;
import com.kamijoucen.ruler.result.RuleValue;
import com.kamijoucen.ruler.runtime.RulerConfiguration;
import com.kamijoucen.ruler.runtime.RuntimeContext;
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
        return run(null);
    }

    public RuleResult run(Map<String, Object> param) {
        if (param == null) {
            param = Collections.emptyMap();
        }
        List<Object> values = null;
        RulerInterpreter interpreter = new RulerInterpreter(module, configuration);
        if (isScript) {
            values = interpreter.runScript(param);
        } else {
            values = interpreter.runExpression(param);
        }

        List<RuleValue> ruleValues = new ArrayList<RuleValue>(values.size());
        for (Object value : values) {
            ruleValues.add(new RuleValue(value));
        }
        return new RuleResult(ruleValues);
    }

    public RuntimeContext customRun(NodeVisitor visitor) {
        AssertUtil.notNull(visitor);
        RulerInterpreter interpreter = new RulerInterpreter(module, configuration);
        return interpreter.runCustomVisitor(visitor);
    }

}
