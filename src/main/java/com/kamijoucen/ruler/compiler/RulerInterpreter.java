package com.kamijoucen.ruler.compiler;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.eval.EvalVisitor;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerProgram;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.AssertUtil;
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

    public List<Object> runExpression(Map<String, Object> param) {
        RulerModule mainModule = program.getMainModule();
        Scope runScope = new Scope("runtime main file", mainModule.getFileScope());
        // 运行上下文
        RuntimeContext context = new RuntimeContext(ConvertUtil.convertToBase(param), new EvalVisitor());
        BaseNode firstNode = CollectionUtil.first(mainModule.getStatements());
        // 执行表达式
        AssertUtil.notNull(firstNode);
        BaseValue value = firstNode.eval(context, runScope);
        return CollectionUtil.list(ConvertRepository.getConverter(value.getType()).baseToReal(value));
    }

    public List<Object> runScript(Map<String, Object> param) {
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
