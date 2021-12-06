package com.kamijoucen.ruler;

import com.kamijoucen.ruler.module.RulerInterpreter;
import com.kamijoucen.ruler.module.RulerProgram;
import com.kamijoucen.ruler.result.RuleResult;
import com.kamijoucen.ruler.result.RuleValue;
import com.kamijoucen.ruler.util.CollectionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RuleRunner {

    private RulerProgram program;

    public RuleRunner(RulerProgram program) {
        this.program = program;
    }

    public RuleResult run() {
        return run(null);
    }

    public RuleResult run(Map<String, Object> param) {
        if (param == null) {
            param = Collections.emptyMap();
        }
        List<Object> values = new RulerInterpreter(program).run(param);

        List<RuleValue> ruleValues = new ArrayList<RuleValue>(values.size());
        for (Object value : values) {
            ruleValues.add(new RuleValue(value));
        }
        return new RuleResult(ruleValues);
    }

}
