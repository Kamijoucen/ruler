package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.BoolValue;
import com.kamijoucen.ruler.domain.value.StringValue;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.logic.util.ConvertUtil;
import com.kamijoucen.ruler.logic.util.NumberUtil;

public class NeOperation implements BinaryOperation {

    private final boolean strict;

    public NeOperation(boolean strict) {
        this.strict = strict;
    }

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);
        return BoolValue.get(!equal(lValue, rValue, context));
    }

    private boolean equal(BaseValue l, BaseValue r, RuntimeContext context) {
        ValueType lt = l.getType();
        ValueType rt = r.getType();

        if (NumberUtil.isNumber(lt) && NumberUtil.isNumber(rt)) {
            return NumberUtil.compareNumbers(l, r) == 0;
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
            if (lt == ValueType.STRING && NumberUtil.isNumber(rt)) {
                try {
                    BaseValue parsed = ConvertUtil.stringToValue(l.toString(), context);
                    if (parsed == null) {
                        return false;
                    }
                    return NumberUtil.compareNumbers(parsed, r) == 0;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            if (NumberUtil.isNumber(lt) && rt == ValueType.STRING) {
                try {
                    BaseValue parsed = ConvertUtil.stringToValue(r.toString(), context);
                    if (parsed == null) {
                        return false;
                    }
                    return NumberUtil.compareNumbers(l, parsed) == 0;
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

}
