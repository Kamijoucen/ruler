package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.*;

public class EqOperation implements BinaryOperation {

    private final boolean strict;

    public EqOperation(boolean strict) {
        this.strict = strict;
    }

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);
        return BoolValue.get(equal(lValue, rValue));
    }

    private boolean equal(BaseValue l, BaseValue r) {
        ValueType lt = l.getType();
        ValueType rt = r.getType();

        if (isNumber(lt) && isNumber(rt)) {
            double lv = lt == ValueType.INTEGER
                    ? ((IntegerValue) l).getValue()
                    : ((DoubleValue) l).getValue();
            double rv = rt == ValueType.INTEGER
                    ? ((IntegerValue) r).getValue()
                    : ((DoubleValue) r).getValue();
            return lv == rv;
        }

        if (lt == ValueType.BOOL && rt == ValueType.BOOL) {
            return ((BoolValue) l).getValue() == ((BoolValue) r).getValue();
        }

        if (lt == ValueType.STRING && rt == ValueType.STRING) {
            return ((StringValue) l).getValue().equals(((StringValue) r).getValue());
        }

        if (lt == ValueType.NULL && rt == ValueType.NULL) {
            return true;
        }

        if (!strict) {
            if (lt == ValueType.STRING && isNumber(rt)) {
                try {
                    double rv = rt == ValueType.INTEGER
                            ? ((IntegerValue) r).getValue()
                            : ((DoubleValue) r).getValue();
                    return Double.parseDouble(l.toString()) == rv;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            if (isNumber(lt) && rt == ValueType.STRING) {
                try {
                    double lv = lt == ValueType.INTEGER
                            ? ((IntegerValue) l).getValue()
                            : ((DoubleValue) l).getValue();
                    return lv == Double.parseDouble(r.toString());
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }

        if (lt == ValueType.NULL || rt == ValueType.NULL) {
            return false;
        }
        if (lt != rt) {
            return false;
        }
        return l.toString().equals(r.toString());
    }

    private boolean isNumber(ValueType type) {
        return type == ValueType.INTEGER || type == ValueType.DOUBLE;
    }
}
