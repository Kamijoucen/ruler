package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.types.module.RulerModule;
import com.kamijoucen.ruler.types.parameter.RulerParameter;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.logic.util.CollectionUtil;
import com.kamijoucen.ruler.logic.convert.ValueConversions;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.NullValue;
import com.kamijoucen.ruler.types.value.ValueType;
import com.kamijoucen.ruler.types.value.ValueConvert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RulerInterpreter {

    private RulerInterpreter() {
    }

    public static List<Object> runStatement(RulerModule module, Scope runScope, RuntimeContext runtimeContext) {
        runtimeContext.clearControlFlags();
        List<BaseValue> values = new ArrayList<>();
        for (BaseNode statement : module.getStatements()) {
            BaseValue value = EvalVisitor.evaluate(statement, runScope, runtimeContext);
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

    public static List<Object> runScript(RulerModule module, Scope runScope, RuntimeContext runtimeContext) {
        return runScript(module, runScope, runtimeContext, true, true);
    }

    public static List<Object> runScriptWithoutGlobalImports(RulerModule module, Scope runScope, RuntimeContext runtimeContext) {
        return runScript(module, runScope, runtimeContext, false, true);
    }

    public static List<Object> runImportModule(RulerModule module, Scope runScope, RuntimeContext runtimeContext) {
        return runScript(module, runScope, runtimeContext, false, false);
    }

    private static List<Object> runScript(
            RulerModule module,
            Scope runScope,
            RuntimeContext runtimeContext,
            boolean includeGlobalModules,
            boolean implicitReturn) {
        RulerConfiguration configuration = runtimeContext.getConfiguration();
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
            lastVal = EvalVisitor.evaluate(statement, runScope, runtimeContext);
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

    private static Object convertToRealValue(BaseValue baseValue) {
        if (baseValue == null) {
            return null;
        }
        if (baseValue.getType() == ValueType.FUNCTION
                || baseValue.getType() == ValueType.CLOSURE) {
            return baseValue;
        }
        ValueConvert convert =
                ValueConversions.getConverter(baseValue.getType());
        if (convert == null) {
            return baseValue;
        }
        return convert.baseToReal(baseValue);
    }

    public static List<Object> runScript(RulerModule module, List<RulerParameter> param, Scope runScope,
                                         RulerConfiguration configuration) {
        Map<String, BaseValue> values = convertParamToBase(param);
        RuntimeContext runtimeContext = configuration.createDefaultRuntimeContext(values);
        return runScript(module, runScope, runtimeContext);
    }

    private static Map<String, BaseValue> convertParamToBase(List<RulerParameter> params) {
        if (CollectionUtil.isEmpty(params)) {
            return Collections.emptyMap();
        }
        Map<String, BaseValue> values = new HashMap<>();
        for (RulerParameter param : params) {
            ValueConvert convert = ValueConversions.getConverter(param.getType());
            if (convert == null) {
                continue;
            }
            BaseValue baseValue = convert.realToBase(param.getValue());
            values.put(param.getName(), baseValue);
        }
        return values;
    }

}
