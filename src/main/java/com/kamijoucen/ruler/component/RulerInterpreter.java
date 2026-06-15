package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.module.RulerModule;
import com.kamijoucen.ruler.domain.parameter.RulerParameter;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.logic.util.CollectionUtil;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.NullValue;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.domain.value.convert.ValueConvert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulerInterpreter {

    private final RulerModule module;
    private final transient RulerConfiguration configuration;

    public RulerInterpreter(RulerModule module, RulerConfiguration configuration) {
        this.module = module;
        this.configuration = configuration;
    }

    public List<Object> runStatement(Scope runScope, RuntimeContext runtimeContext) {
        runtimeContext.clearControlFlags();
        List<BaseValue> values = new ArrayList<>();
        for (BaseNode statement : module.getStatements()) {
            BaseValue value = statement.eval(runScope, runtimeContext);
            values.add(value);
            runtimeContext.clearControlFlags();
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
        return runScript(runScope, runtimeContext, true, true);
    }

    public List<Object> runScriptWithoutGlobalImports(Scope runScope, RuntimeContext runtimeContext) {
        return runScript(runScope, runtimeContext, false, true);
    }

    public List<Object> runImportModule(Scope runScope, RuntimeContext runtimeContext) {
        return runScript(runScope, runtimeContext, false, false);
    }

    private List<Object> runScript(
            Scope runScope,
            RuntimeContext runtimeContext,
            boolean includeGlobalModules,
            boolean implicitReturn) {
        List<BaseNode> userStatements = module.getStatements();
        List<BaseNode> allNode = new ArrayList<>(
                userStatements.size() + configuration.getGlobalImportModules().size());
        if (includeGlobalModules) {
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
        List<BaseValue> returnValue = runtimeContext.getReturnSpace();
        runtimeContext.clearReturnState();

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

    public List<Object> runScript(List<RulerParameter> param, Scope runScope) {
        Map<String, BaseValue> values = convertParamToBase(param);
        RuntimeContext runtimeContext = configuration.createDefaultRuntimeContext(values);
        return this.runScript(runScope, runtimeContext);
    }

    private Map<String, BaseValue> convertParamToBase(List<RulerParameter> params) {
        if (CollectionUtil.isEmpty(params)) {
            return Collections.emptyMap();
        }
        Map<String, BaseValue> values = new HashMap<>();
        for (RulerParameter param : params) {
            ValueConvert convert = configuration.getValueConvertManager().getConverter(param.getType());
            if (convert == null) {
                continue;
            }
            BaseValue baseValue = convert.realToBase(param.getValue(), configuration);
            values.put(param.getName(), baseValue);
        }
        return values;
    }

}
