package com.kamijoucen.ruler.common;

import com.kamijoucen.ruler.value.BaseValue;

public class EvalResult {

    public final BaseValue value;

    public final BaseValue selfValue;

    public final FlowControl control;

    public EvalResult(BaseValue value, BaseValue selfValue, FlowControl control) {
        this.value = value;
        this.selfValue = selfValue;
        this.control = control;
    }

    public static EvalResult value(BaseValue value, BaseValue selfValue, FlowControl control) {
        return new EvalResult(value, selfValue, control);
    }

    public static EvalResult value(BaseValue value, BaseValue selfValue) {
        return new EvalResult(value, selfValue, FlowControl.NONE);
    }

    public static EvalResult value(BaseValue value) {
        return new EvalResult(value, null, FlowControl.NONE);
    }
}
