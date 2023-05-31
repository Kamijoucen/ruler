package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.DefaultParamValNode;
import com.kamijoucen.ruler.ast.facotr.NameNode;
import com.kamijoucen.ruler.common.Constant;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.*;
import com.kamijoucen.ruler.value.constant.NullValue;

import java.util.Arrays;
import java.util.List;

public class CallOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue callFunc = lhs.eval(scope, context);
        BaseValue self = context.getCurrentSelfValue();
        context.getStackDepthCheckOperation().addDepth(context);
        try {
            Object[] funcParam = Arrays.copyOfRange(params, 0, params.length);
            if (callFunc.getType() == ValueType.FUNCTION) {
                RulerFunction function = ((FunctionValue) callFunc).getValue();
                return (BaseValue) function.call(context, self, funcParam);
            } else if (callFunc.getType() == ValueType.CLOSURE) {
                ClosureValue function = ((ClosureValue) callFunc);
                return callClosure(context, self, function, (BaseValue[]) funcParam);
            } else {
                throw new IllegalArgumentException(callFunc + " not is a function!");
            }
        } finally {
            context.getStackDepthCheckOperation().subDepth(context);
        }
    }

    private BaseValue callClosure(RuntimeContext context, BaseValue self, ClosureValue closure, BaseValue[] funcParam) {
        Scope callScope = new Scope("closure", closure.getDefineScope());
        callScope.initReturnSpace();
        if (self != null) {
            callScope.putLocal(Constant.THIS_ARG, self);
        }
        // put args in scope
        callScope.putLocal(Constant.FUN_ARG_LIST, new ArrayValue(Arrays.asList(funcParam)));

        List<BaseNode> defineParam = closure.getParam();
        for (int i = 0; i < defineParam.size(); i++) {
            BaseNode paramNode = defineParam.get(i);
            if (paramNode instanceof NameNode) {
                NameNode nameNode = (NameNode) defineParam.get(i);
                callScope.putLocal(nameNode.name.name, i >= funcParam.length ? NullValue.INSTANCE : funcParam[i]);
            } else if (paramNode instanceof DefaultParamValNode) {
                DefaultParamValNode defParamNode = (DefaultParamValNode) paramNode;
                if (i >= funcParam.length) {
                    String paramName = defParamNode.getName().name.name;
                    BaseValue defValue = defParamNode.getExp().eval(callScope, context);
                    callScope.putLocal(paramName, defValue);
                } else {
                    callScope.putLocal(defParamNode.getName().name.name, funcParam[i]);
                }
            } else {
                throw new IllegalArgumentException();
            }
        }

        // call function
        closure.getBlock().eval(callScope, context);

        // get return value
        List<BaseValue> returnValues = callScope.getReturnSpace();
        if (returnValues == null || returnValues.size() == 0) {
            return NullValue.INSTANCE;
        }
        if (returnValues.size() == 1) {
            return returnValues.get(0);
        }
        return new ArrayValue(returnValues);
    }

}
