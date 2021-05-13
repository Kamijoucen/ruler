package com.kamijoucen.ruler;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.env.DefaultScope;
import com.kamijoucen.ruler.env.Scope;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RuleScript {

    private Scope scope;

    private List<BaseAST> asts;

    public RuleScript(Scope globalScope, List<BaseAST> asts) {
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

        List<Object> returnValue = interpreter.run(param);

        return null;
    }

}
