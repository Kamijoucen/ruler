package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;

public class AndOperation implements LogicOperation {

    @Override
    public BaseValue compute(RuntimeContext context, Scope scope, BaseNode... param) {
        BaseNode exp1 = param[0];
        BaseNode exp2 = param[1];
        BaseValue tempExp1Val = exp1.eval(context, scope);
        if (tempExp1Val.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("该值不支持&&:" + tempExp1Val);
        }
        BoolValue exp1Val = (BoolValue) tempExp1Val;
        if (!exp1Val.getValue()) {
            return BoolValue.get(false);
        }
        BaseValue tempExp2Val = exp2.eval(context, scope);
        if (tempExp2Val.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("该值不支持&&:" + tempExp2Val);
        }
        BoolValue exp2Val = (BoolValue) tempExp2Val;
        if (!exp2Val.getValue()) {
            return BoolValue.get(false);
        }
        return BoolValue.get(true);
    }
}
