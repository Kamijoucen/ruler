package com.kamijoucen.ruler;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.compiler.RulerInterpreter;
import com.kamijoucen.ruler.config.RuntimeConfiguration;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.result.RuleResult;
import com.kamijoucen.ruler.result.RuleValue;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.util.AssertUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RuleRunner {

    private RulerModule module;
    private boolean isScript;
    private RuntimeConfiguration runtimeConfiguration;

    public RuleRunner(RulerModule module, boolean isScript) {
        this.module = module;
        this.isScript = isScript;
        this.runtimeConfiguration = null;
    }

    public RuleResult run() {
        return run(null);
    }

    public RuleResult run(Map<String, Object> param) {
        if (param == null) {
            param = Collections.emptyMap();
        }
        List<Object> values = null;
        RulerInterpreter interpreter = new RulerInterpreter(module);
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
        RulerInterpreter interpreter = new RulerInterpreter(module);
        return interpreter.runCustomVisitor(visitor);
    }

}
