package com.kamijoucen.ruler.module;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.eval.EvalVisitor;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.util.ConvertUtil;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RulerInterpreter {

    private final RulerProgram program;

    public RulerInterpreter(RulerProgram program) {
        this.program = program;
    }

    public List<Object> run(Map<String, Object> param) {
        RulerModule mainModule = program.getMainModule();
        Scope runScope = new Scope("runtime main file", mainModule.getFileScope());
        runScope.initReturnSpace();

        RuntimeContext context = new RuntimeContext(ConvertUtil.convertToBase(param), new EvalVisitor());
        for (BaseNode statement : mainModule.getStatements()) {
            statement.eval(context, runScope);
        }
        List<BaseValue> returnValue = runScope.getReturnSpace();
        if (CollectionUtil.isEmpty(returnValue)) {
            return Collections.emptyList();
        }
        List<Object> realValue = new ArrayList<Object>(returnValue.size());
        for (BaseValue baseValue : returnValue) {
            realValue.add(ConvertRepository.getConverter(baseValue.getType()).baseToReal(baseValue));
        }
        return realValue;
    }




}
