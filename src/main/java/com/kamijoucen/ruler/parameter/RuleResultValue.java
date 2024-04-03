package com.kamijoucen.ruler.parameter;

import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.ClosureValue;
import com.kamijoucen.ruler.value.FunctionValue;

public class RuleResultValue {

    public final Object value;

    public RuleResultValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public String toFormattedString() {
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        if (value == null) {
            return "null";
        }
        if (value instanceof FunctionValue) {
            return "function: fun " + ((FunctionValue) value).getValue().getName() + "(...)";
        }
        if (value instanceof ClosureValue) {
            String funName = ((ClosureValue) value).getName();
            return "function: fun " + (IOUtil.isBlank(funName) ? value.hashCode() : funName)
                    + "(...)";
        }
        return value.toString();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public boolean toBoolean() {
        return (Boolean) value;
    }

    public long toInteger() {
        return (long) value;
    }

    public double toDouble() {
        return (Double) value;
    }

}
