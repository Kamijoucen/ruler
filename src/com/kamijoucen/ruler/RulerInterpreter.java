package com.kamijoucen.ruler;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.env.DefaultScope;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;
import java.util.Map;

class RulerInterpreter {

    private List<BaseAST> asts;

    private Scope scope;

    public RulerInterpreter(List<BaseAST> asts, Scope scope) {
        this.asts = asts;
        this.scope = new DefaultScope(scope);
    }

    public List<Object> run(Map<String, Object> param) {

        scope.setReturnSpace();

        for (BaseAST ast : asts) {
            ast.eval(scope);
        }

        Map<String, BaseValue> returnValue = scope.getReturnSpace();
        return null;
    }

}
