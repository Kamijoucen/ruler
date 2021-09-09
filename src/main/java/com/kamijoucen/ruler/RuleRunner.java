package com.kamijoucen.ruler;

import com.kamijoucen.ruler.module.RulerProgram;
import com.kamijoucen.ruler.module.RulerInterpreter;
import com.kamijoucen.ruler.runtime.Scope;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RuleRunner {

    private RulerProgram program;

    public RuleRunner(RulerProgram program) {
        this.program = program;
    }

    public List<Object> run() {
        return run(null);
    }

    public List<Object> run(Map<String, Object> param) {
        if (param == null) {
            param = Collections.emptyMap();
        }
        RulerInterpreter interpreter = new RulerInterpreter(program);

        return interpreter.run(param);
    }

}
