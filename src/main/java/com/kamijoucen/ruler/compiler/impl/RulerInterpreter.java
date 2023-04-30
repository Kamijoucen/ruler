package com.kamijoucen.ruler.compiler.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.parameter.RulerParameter;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.util.ConvertUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RulerInterpreter {

    private final RulerModule module;
    private transient RulerConfiguration configuration;
    private Boolean hasImportGlobalModule = true;

    private RuntimeContext runtimeContext;

    public RulerInterpreter(RulerModule module, RulerConfiguration configuration) {
        this.module = module;
        this.configuration = configuration;
    }

    public RuntimeContext runCustomVisitor(NodeVisitor visitor, Scope runScope) {

        this.runtimeContext = configuration.createDefaultRuntimeContext(null);
        this.runtimeContext.setNodeVisitor(visitor);

        for (BaseNode statement : module.getStatements()) {
            statement.eval(runScope, this.runtimeContext);
        }
        return this.runtimeContext;
    }

    public List<Object> runExpression(List<RulerParameter> param, Scope runScope) {
        runScope.initReturnSpace();

        Map<String, BaseValue> values = ConvertUtil.convertParamToBase(param, configuration);

        this.runtimeContext = configuration.createDefaultRuntimeContext(values);

        if (hasImportGlobalModule) {
            List<ImportNode> globalImportModules = configuration.getGlobalImportModules();
            if (CollectionUtil.isNotEmpty(globalImportModules)) {
                for (ImportNode node : globalImportModules) {
                    node.eval(runScope, this.runtimeContext);
                }
            }
        }
        BaseNode firstNode = CollectionUtil.first(module.getStatements());
        // 执行表达式
        AssertUtil.notNull(firstNode);
        BaseValue value = firstNode.eval(runScope, this.runtimeContext);
        return CollectionUtil.list(ConvertRepository.getConverter(value.getType()).baseToReal(value, configuration));
    }

    public List<Object> runScript(List<RulerParameter> param, Scope runScope) {
        runScope.initReturnSpace();

        Map<String, BaseValue> values = ConvertUtil.convertParamToBase(param, configuration);
        this.runtimeContext = configuration.createDefaultRuntimeContext(values);

        List<BaseNode> allNode = new ArrayList<BaseNode>(
                module.getStatements().size() + configuration.getGlobalImportModules().size());
        if (hasImportGlobalModule) {
            allNode.addAll(configuration.getGlobalImportModules());
        }
        allNode.addAll(module.getStatements());

        for (BaseNode statement : allNode) {
            if (statement.eval(runScope, this.runtimeContext).getType() == ValueType.RETURN) {
                break;
            }
        }
        List<BaseValue> returnValue = runScope.getReturnSpace();
        if (CollectionUtil.isEmpty(returnValue)) {
            return Collections.emptyList();
        }
        List<Object> realValue = new ArrayList<Object>(returnValue.size());
        for (BaseValue baseValue : returnValue) {
            realValue.add(ConvertRepository.getConverter(baseValue.getType()).baseToReal(baseValue, configuration));
        }
        return realValue;
    }

    public Boolean getHasImportGlobalModule() {
        return hasImportGlobalModule;
    }

    public void setHasImportGlobalModule(Boolean hasImportGlobalModule) {
        this.hasImportGlobalModule = hasImportGlobalModule;
    }

    public RuntimeContext getRuntimeContext() {
        return runtimeContext;
    }
}
