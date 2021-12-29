package com.kamijoucen.ruler.typecheck;

import com.kamijoucen.ruler.ast.facotr.LogicBinaryOperationNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.type.BoolType;
import com.kamijoucen.ruler.type.FailureType;
import com.kamijoucen.ruler.type.UnknowType;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class LogicBinaryChecker implements BaseEval<LogicBinaryOperationNode> {

    @Override
    public BaseValue eval(LogicBinaryOperationNode node, Scope scope, RuntimeContext context) {
        BaseValue typeVal1 = node.getExp1().typeCheck(context, scope);
        BaseValue typeVal2 = node.getExp2().typeCheck(context, scope);

        if (!isAvailableType(typeVal1) || !isAvailableType(typeVal2)) {
            return FailureType.INSTANCE;
        }
        if (typeVal1.getType() == ValueType.UN_KNOW
                || typeVal2.getType() == ValueType.UN_KNOW) {
            return UnknowType.INSTANCE;
        }
        return BoolType.INSTANCE;
    }

    private boolean isAvailableType(BaseValue val) {
        return val.getType() == ValueType.BOOL || val.getType() == ValueType.UN_KNOW;
    }

}
