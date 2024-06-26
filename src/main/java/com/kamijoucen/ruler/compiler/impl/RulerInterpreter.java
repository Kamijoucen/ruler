package com.kamijoucen.ruler.compiler.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ImportNode;
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
import com.kamijoucen.ruler.value.convert.ValueConvert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RulerInterpreter {

    private final RulerModule module;
    private final transient RulerConfiguration configuration;
    private boolean hasImportGlobalModule = true;

    public RulerInterpreter(RulerModule module, RulerConfiguration configuration) {
        this.module = module;
        this.configuration = configuration;
    }

    public List<Object> runExpression(List<RulerParameter> param, Scope runScope) {
        Map<String, BaseValue> values = ConvertUtil.convertParamToBase(param, configuration);
        RuntimeContext runtimeContext = configuration.createDefaultRuntimeContext(values);
        if (hasImportGlobalModule) {
            List<ImportNode> globalImportModules = configuration.getGlobalImportModules();
            if (CollectionUtil.isNotEmpty(globalImportModules)) {
                for (ImportNode node : globalImportModules) {
                    node.eval(runScope, runtimeContext);
                }
            }
        }
        BaseNode firstNode = CollectionUtil.first(module.getStatements());
        // 执行表达式
        AssertUtil.notNull(firstNode);
        BaseValue value = firstNode.eval(runScope, runtimeContext);

        ValueConvert convert =
                this.configuration.getValueConvertManager().getConverter(value.getType());
        return CollectionUtil.list(convert.baseToReal(value, configuration));
    }

    public List<Object> runStatement(Scope runScope, RuntimeContext runtimeContext) {
        List<BaseValue> values = new ArrayList<>();
        for (BaseNode statement : module.getStatements()) {
            BaseValue value = statement.eval(runScope, runtimeContext);
            values.add(value);
        }
        if (CollectionUtil.isEmpty(values)) {
            return Collections.emptyList();
        }
        List<Object> realValue = new ArrayList<>(values.size());
        for (BaseValue baseValue : values) {
            // TODO 临时处理函数和闭包的返回值
            if (baseValue.getType() == ValueType.FUNCTION
                    || baseValue.getType() == ValueType.CLOSURE) {
                realValue.add(baseValue);
            } else {
                ValueConvert convert = this.configuration.getValueConvertManager()
                        .getConverter(baseValue.getType());
                realValue.add(convert.baseToReal(baseValue, configuration));
            }
        }
        return realValue;
    }

    public List<Object> runScript(Scope runScope, RuntimeContext runtimeContext) {
        List<BaseNode> allNode = new ArrayList<>(
                module.getStatements().size() + configuration.getGlobalImportModules().size());
        if (hasImportGlobalModule) {
            allNode.addAll(configuration.getGlobalImportModules());
        }
        allNode.addAll(module.getStatements());

        for (BaseNode statement : allNode) {
            statement.eval(runScope, runtimeContext);
            if (runtimeContext.isReturnFlag()) {
                break;
            }
        }
        runtimeContext.setReturnFlag(false);
        List<BaseValue> returnValue = runtimeContext.getReturnSpace();
        runtimeContext.clearReturnSpace();

        if (CollectionUtil.isEmpty(returnValue)) {
            return Collections.emptyList();
        }
        List<Object> realValue = new ArrayList<>(returnValue.size());
        for (BaseValue baseValue : returnValue) {
            ValueConvert convert =
                    this.configuration.getValueConvertManager().getConverter(baseValue.getType());
            realValue.add(convert.baseToReal(baseValue, configuration));
        }
        return realValue;
    }

    public List<Object> runScript(List<RulerParameter> param, Scope runScope) {
        Map<String, BaseValue> values = ConvertUtil.convertParamToBase(param, configuration);
        RuntimeContext runtimeContext = configuration.createDefaultRuntimeContext(values);
        return this.runScript(runScope, runtimeContext);
    }

    public Boolean getHasImportGlobalModule() {
        return hasImportGlobalModule;
    }

    public void setHasImportGlobalModule(boolean hasImportGlobalModule) {
        this.hasImportGlobalModule = hasImportGlobalModule;
    }

}
