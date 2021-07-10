package com.kamijoucen.ruler;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.DefaultScope;
import com.kamijoucen.ruler.runtime.Scope;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RuleScript {

    private Scope scope;

    private List<BaseNode> asts;

    public RuleScript(Scope globalScope, List<BaseNode> asts) {
        this.asts = asts;
        this.scope = new DefaultScope(globalScope, true, true);
    }

    public List<Object> run() {
        return run(null);
    }

    public List<Object> run(Map<String, Object> param) {
        if (param == null) {
            param = Collections.emptyMap();
        }
        RulerInterpreter interpreter = new RulerInterpreter(asts, scope);

        return interpreter.run(param);
    }

}
