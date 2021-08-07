package com.kamijoucen.ruler;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.ConvertUtil;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class RulerInterpreter {

    private List<BaseNode> asts;

    private Scope fileScope;

    public RulerInterpreter(List<BaseNode> asts, Scope fileScope) {
        this.asts = asts;
        this.fileScope = fileScope;
    }

    public List<Object> run(Map<String, Object> param) {

//        Scope runScope = new Scope("run", fileScope, ConvertUtil.convertToBase(param));
        Scope runScope = new Scope("run", fileScope);

        runScope.initReturnSpace();

        for (BaseNode ast : asts) {
            ast.eval(runScope);
        }
        List<BaseValue> returnValue = runScope.getReturnSpace();

        if (CollectionUtil.isEmpty(returnValue)) {
            return Collections.emptyList();
        }
        List<Object> realValue = new ArrayList<Object>(returnValue.size());

        for (BaseValue baseValue : returnValue) {
            realValue.add(ConvertRepository.getConverter(baseValue.getType()));
        }
        return realValue;
    }

}
