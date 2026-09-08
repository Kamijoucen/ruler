package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.logic.eval.EvalVisitor;
import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.*;
import com.kamijoucen.ruler.logic.util.NumberUtil;

public class EqOperation implements BinaryOperation {

    private final boolean strict;

    public EqOperation(boolean strict) {
        this.strict = strict;
    }

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue lValue = EvalVisitor.evaluate(lhs, scope, context);
        BaseValue rValue = EvalVisitor.evaluate(rhs, scope, context);
        return BoolValue.get(equal(lValue, rValue, context));
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
                    BaseValue parsed = com.kamijoucen.ruler.logic.util.ConvertUtil.stringToValue(l.toString(), context);
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
                    BaseValue parsed = com.kamijoucen.ruler.logic.util.ConvertUtil.stringToValue(r.toString(), context);
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
