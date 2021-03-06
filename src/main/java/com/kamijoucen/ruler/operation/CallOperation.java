package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.facotr.NameNode;
import com.kamijoucen.ruler.common.Constant;
import com.kamijoucen.ruler.common.RMetaInfo;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.*;
import com.kamijoucen.ruler.value.constant.NullValue;

import java.util.Arrays;
import java.util.List;

public class CallOperation implements Operation {

    @Override
    public BaseValue compute(RuntimeContext context, BaseValue... param) {
        BaseValue func = param[0];

        context.getStackDepthCheckOperation().addDepth(context);
        try {
            Object[] funcParam = Arrays.copyOfRange(param, 1, param.length);
            if (func.getType() == ValueType.FUNCTION) {
                RulerFunction function = ((FunctionValue) func).getValue();
                return (BaseValue) function.call(funcParam);
            }
            if (func.getType() == ValueType.CLOSURE) {
                ClosureValue function = ((ClosureValue) func);
                return callClosure(context, function, (BaseValue[]) funcParam);
            }
        } finally {
            context.getStackDepthCheckOperation().subDepth(context);
        }
        throw SyntaxException.withSyntax(func + " 不是一个函数");
    }

    private BaseValue callClosure(RuntimeContext context, ClosureValue closure, BaseValue[] funcParam) {
        Scope callScope = new Scope("closure", closure.getDefineScope());
        List<BaseNode> defineParam = closure.getParam();
        for (int i = 0; i < defineParam.size(); i++) {
            NameNode name = (NameNode) defineParam.get(i);
            callScope.putLocal(name.name.name, i >= funcParam.length ? NullValue.INSTANCE : funcParam[i]);
        }
        callScope.initReturnSpace();
        // put args in scope
        callScope.putLocal(Constant.FUN_ARG_LIST, new ArrayValue(Arrays.asList(funcParam), new RMetaInfo()));
        BaseNode block = closure.getBlock();
        block.eval(context, callScope);
        List<BaseValue> returnValues = callScope.getReturnSpace();
        if (returnValues == null || returnValues.size() == 0) {
            return NullValue.INSTANCE;
        }
        if (returnValues.size() == 1) {
            return returnValues.get(0);
        }
        return new ArrayValue(returnValues, new RMetaInfo());
    }

}
