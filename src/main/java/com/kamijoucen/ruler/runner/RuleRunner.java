package com.kamijoucen.ruler.runner;

import com.kamijoucen.ruler.compiler.RulerInterpreter;
import com.kamijoucen.ruler.module.RulerProgram;
import com.kamijoucen.ruler.result.RuleResult;
import com.kamijoucen.ruler.result.RuleValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RuleRunner {

    private RulerProgram program;
    private boolean isScript;

    public RuleRunner(RulerProgram program, boolean isScript) {
        this.program = program;
        this.isScript = isScript;
    }

    public RuleResult run() {
        return run(null);
    }

    public RuleResult run(Map<String, Object> param) {
        if (param == null) {
            param = Collections.emptyMap();
        }
        List<Object> values = null;
        RulerInterpreter interpreter = new RulerInterpreter(program);
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

}
