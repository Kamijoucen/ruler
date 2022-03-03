package com.kamijoucen.ruler.compiler;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.runtime.RulerConfiguration;
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

    private final RulerModule module;
    private RulerConfiguration configuration;

    public RulerInterpreter(RulerModule module, RulerConfiguration configuration) {
        this.module = module;
        this.configuration = configuration;
    }

    public RuntimeContext runCustomVisitor(NodeVisitor visitor) {
        Scope runScope = new Scope("runtime main file", module.getFileScope());
        RuntimeContext context = new RuntimeContext(null, visitor, configuration.getTypeCheckVisitor(),
                configuration.getImportCache(), configuration);
        for (BaseNode statement : module.getStatements()) {
            statement.eval(context, runScope);
        }
        return context;
    }

    public List<Object> runExpression(Map<String, Object> param) {
        Scope runScope = new Scope("runtime main file", module.getFileScope());
        // 运行上下文
        RuntimeContext context = new RuntimeContext(ConvertUtil.convertToBase(param), configuration.getEvalVisitor(),
                configuration.getTypeCheckVisitor(), configuration.getImportCache(), configuration);
        BaseNode firstNode = CollectionUtil.first(module.getStatements());
        // 执行表达式
        AssertUtil.notNull(firstNode);
        BaseValue value = firstNode.eval(context, runScope);
        return CollectionUtil.list(ConvertRepository.getConverter(value.getType()).baseToReal(value));
    }

    public List<Object> runScript(Map<String, Object> param) {
        Scope runScope = new Scope("runtime main file", module.getFileScope());
        runScope.initReturnSpace();

        RuntimeContext context = new RuntimeContext(ConvertUtil.convertToBase(param), configuration.getEvalVisitor(),
                configuration.getTypeCheckVisitor(), configuration.getImportCache(), configuration);
        for (BaseNode statement : module.getStatements()) {
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
