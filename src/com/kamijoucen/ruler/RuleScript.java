package com.kamijoucen.ruler;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.env.DefaultScope;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.runtime.RulerFunction;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RuleScript {

    private Scope scope;

    private List<BaseAST> asts;

    public RuleScript(Scope globalScope, List<BaseAST> asts) {
        this.asts = asts;
        this.scope = new DefaultScope(globalScope, new ConcurrentHashMap<String, BaseValue>(), new ConcurrentHashMap<String, RulerFunction>());
    }


    public List<Object> run() {
        return run(null);
    }

    public List<Object> run(Map<String, Object> param) {
        if (param == null) {
            param = Collections.emptyMap();
        }
        RulerInterpreter interpreter = new RulerInterpreter(asts, scope);

        List<Object> returnValue = interpreter.run(param);

        return null;
    }

}
