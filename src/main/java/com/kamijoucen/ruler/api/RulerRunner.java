package com.kamijoucen.ruler.api;
import com.kamijoucen.ruler.types.module.RulerModule;

import com.kamijoucen.ruler.logic.convert.ValueConversions;
import com.kamijoucen.ruler.logic.eval.RulerInterpreter;
import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.types.parameter.RuleResultValue;
import com.kamijoucen.ruler.types.parameter.RulerParameter;
import com.kamijoucen.ruler.types.parameter.RulerResult;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.ValueType;
import com.kamijoucen.ruler.types.value.ValueConvert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RulerRunner implements Serializable {

    private static final long serialVersionUID = 1L;

    private final RulerModule module;
    private transient RulerConfiguration configuration;

    public RulerRunner(RulerModule module, RulerConfiguration configuration) {
        this.module = module;
        this.configuration = configuration;
    }

    public RulerResult run(List<RulerParameter> param, RulerConfiguration configuration) {
        List<Object> values;
        values = RulerInterpreter.runScript(module, param,
                new Scope("runtime root", true, configuration.getGlobalScope(), null), configuration);

        List<RuleResultValue> ruleResultValues = new ArrayList<>(values.size());
        for (Object value : values) {
            ruleResultValues.add(new RuleResultValue(value));
        }
        return new RulerResult(ruleResultValues);
    }

    public RulerResult run() {
        return run((Map<String, Object>) null);
    }

    public RulerResult run(List<RulerParameter> param) {
        return run(param, configuration);
    }

    public RulerResult run(Map<String, Object> param) {
        if (param == null) {
            param = Collections.emptyMap();
        }
        List<RulerParameter> parameter = processParamTypes(param);
        return run(parameter);
    }

    private List<RulerParameter> processParamTypes(Map<String, Object> param) {
        if (param.isEmpty()) {
            return Collections.emptyList();
        }
        List<RulerParameter> list = new ArrayList<>(param.size());
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            list.add(processOneParam(entry));
        }
        return list;
    }

    private RulerParameter processOneParam(Map.Entry<String, Object> entry) {
        Object value = entry.getValue();
        if (value == null) {
            return new RulerParameter(ValueType.NULL, entry.getKey(), null);
        }
        if (value.getClass().isArray()) {
            return new RulerParameter(ValueType.ARRAY, entry.getKey(), value);
        }
        if (value instanceof List) {
            return new RulerParameter(ValueType.ARRAY, entry.getKey(), value);
        }

        ValueConvert convert = ValueConversions.getConverter(value);
        if (convert == null) {
            throw new IllegalArgumentException("unsupported parameter type: " + value.getClass());
        }
        return new RulerParameter(convert.getType(), entry.getKey(), value);
    }

    public RulerConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RulerConfiguration configuration) {
        this.configuration = configuration;
    }

    public RulerModule getModule() {
        return module;
    }

}
