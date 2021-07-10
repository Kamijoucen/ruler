package com.kamijoucen.ruler;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.DefaultScope;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;
import java.util.Map;

class RulerInterpreter {

    private List<BaseNode> asts;

    private Scope scope;

    public RulerInterpreter(List<BaseNode> asts, Scope scope) {
        this.asts = asts;
        this.scope = new DefaultScope(scope);
    }

    public List<Object> run(Map<String, Object> param) {

        scope.initReturnSpace();

        for (BaseNode ast : asts) {
            ast.eval(scope);
        }

        List<BaseValue> returnValue = scope.getReturnSpace();

        return null;
    }

}
