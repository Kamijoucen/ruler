package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.*;

public class EqOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);
        if (lValue.getType() == ValueType.INTEGER && rValue.getType() == ValueType.INTEGER) {
            IntegerValue val1 = (IntegerValue) lValue;
            IntegerValue val2 = (IntegerValue) rValue;
            return BoolValue.get(val1.getValue() == val2.getValue());
        } else if (lValue.getType() == ValueType.INTEGER && rValue.getType() == ValueType.DOUBLE) {
            IntegerValue val1 = (IntegerValue) lValue;
            DoubleValue val2 = (DoubleValue) rValue;
            return BoolValue.get(val1.getValue() == val2.getValue());
        } else if (lValue.getType() == ValueType.DOUBLE && rValue.getType() == ValueType.INTEGER) {
            DoubleValue val1 = (DoubleValue) lValue;
            IntegerValue val2 = (IntegerValue) rValue;
            return BoolValue.get(val1.getValue() == val2.getValue());
        } else if (lValue.getType() == ValueType.DOUBLE && rValue.getType() == ValueType.DOUBLE) {
            DoubleValue val1 = (DoubleValue) lValue;
            DoubleValue val2 = (DoubleValue) rValue;
            return BoolValue.get(val1.getValue() == val2.getValue());
        } else {
            if (lValue.getType() == ValueType.NULL && rValue.getType() == ValueType.NULL) {
                return BoolValue.get(true);
            }
            if (lValue.getType() == ValueType.NULL || rValue.getType() == ValueType.NULL) {
                return BoolValue.get(false);
            }
            if (lValue.getType() == ValueType.STRING || rValue.getType() == ValueType.STRING) {
                String strVal1 = lValue.toString();
                String strVal2 = rValue.toString();
                return BoolValue.get(strVal1.equals(strVal2));
            }
            if (lValue.getType() != rValue.getType()) {
                return BoolValue.get(false);
            }
            String strVal1 = lValue.toString();
            String strVal2 = rValue.toString();
            return BoolValue.get(strVal1.equals(strVal2));
        }
    }
}
