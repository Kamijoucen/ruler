package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.ImportNode;
import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.module.RulerModule;
import com.kamijoucen.ruler.domain.parameter.RulerParameter;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.logic.util.AssertUtil;
import com.kamijoucen.ruler.logic.util.CollectionUtil;
import com.kamijoucen.ruler.logic.util.ConvertUtil;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.NullValue;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.domain.value.convert.ValueConvert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RulerInterpreter {

    private final RulerModule module;
    private final transient RulerConfiguration configuration;
    private boolean hasImportGlobalModule = true;
    private boolean implicitReturn = true;

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
        return CollectionUtil.list(convertToRealValue(value));
    }

    public List<Object> runStatement(Scope runScope, RuntimeContext runtimeContext) {
        clearControlFlags(runtimeContext);
        List<BaseValue> values = new ArrayList<>();
        for (BaseNode statement : module.getStatements()) {
            BaseValue value = statement.eval(runScope, runtimeContext);
            values.add(value);
            clearControlFlags(runtimeContext);
        }
        if (CollectionUtil.isEmpty(values)) {
            return Collections.emptyList();
        }
        List<Object> realValue = new ArrayList<>(values.size());
        for (BaseValue baseValue : values) {
            realValue.add(convertToRealValue(baseValue));
        }
        return realValue;
    }

    public List<Object> runScript(Scope runScope, RuntimeContext runtimeContext) {
        List<BaseNode> userStatements = module.getStatements();
        List<BaseNode> allNode = new ArrayList<>(
                userStatements.size() + configuration.getGlobalImportModules().size());
        if (hasImportGlobalModule) {
            allNode.addAll(configuration.getGlobalImportModules());
        }
        allNode.addAll(userStatements);
        boolean hasUserStatement = !userStatements.isEmpty();

        BaseValue lastVal = NullValue.INSTANCE;
        for (BaseNode statement : allNode) {
            lastVal = statement.eval(runScope, runtimeContext);
            if (runtimeContext.isReturnFlag()) {
                break;
            }
        }
        boolean wasReturn = runtimeContext.isReturnFlag();
        runtimeContext.setReturnFlag(false);
        List<BaseValue> returnValue = runtimeContext.getReturnSpace();
        runtimeContext.clearReturnSpace();

        if (implicitReturn && !wasReturn && hasUserStatement && CollectionUtil.isEmpty(returnValue)) {
            returnValue = Collections.singletonList(lastVal);
        }

        if (CollectionUtil.isEmpty(returnValue)) {
            return Collections.emptyList();
        }
        List<Object> realValue = new ArrayList<>(returnValue.size());
        for (BaseValue baseValue : returnValue) {
            realValue.add(convertToRealValue(baseValue));
        }
        return realValue;
    }

    private Object convertToRealValue(BaseValue baseValue) {
        if (baseValue == null) {
            return null;
        }
        if (baseValue.getType() == ValueType.FUNCTION
                || baseValue.getType() == ValueType.CLOSURE) {
            return baseValue;
        }
        ValueConvert convert =
                this.configuration.getValueConvertManager().getConverter(baseValue.getType());
        if (convert == null) {
            return baseValue;
        }
        return convert.baseToReal(baseValue, configuration);
    }

    private void clearControlFlags(RuntimeContext runtimeContext) {
        runtimeContext.setBreakFlag(false);
        runtimeContext.setContinueFlag(false);
        runtimeContext.setReturnFlag(false);
        runtimeContext.clearReturnSpace();
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

    public boolean isImplicitReturn() {
        return implicitReturn;
    }

    public void setImplicitReturn(boolean implicitReturn) {
        this.implicitReturn = implicitReturn;
    }

}
