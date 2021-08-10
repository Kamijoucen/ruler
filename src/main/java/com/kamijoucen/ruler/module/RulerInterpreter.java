package com.kamijoucen.ruler.module;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.util.ConvertUtil;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RulerInterpreter {

    private RulerFile file;

    private Scope fileScope;

    public RulerInterpreter(RulerFile file, Scope fileScope) {
        this.file = file;
        this.fileScope = fileScope;
    }

    public List<Object> run(Map<String, Object> param) {

        Scope runScope = new Scope("run", fileScope, ConvertUtil.convertToBase(param));

        runScope.initReturnSpace();

        for (BaseNode ast : file.getStatements()) {
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
