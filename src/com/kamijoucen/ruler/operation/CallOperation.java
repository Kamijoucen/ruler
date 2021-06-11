package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RulerFunction;
import com.kamijoucen.ruler.util.Assert;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;
import com.kamijoucen.ruler.value.FunctionValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.constant.NullValue;

import java.util.Arrays;
import java.util.List;

public class CallOperation implements Operation {

    @Override
    public BaseValue compute(BaseValue... param) {

        BaseValue func = param[0];

        Object[] funcParam = Arrays.copyOfRange(param, 1, param.length);

        if (func.getType() == ValueType.FUNCTION) {
            RulerFunction function = ((FunctionValue) func).getValue();
            return (BaseValue) function.call(funcParam);
        }

        if (func.getType() == ValueType.CLOSURE) {
            ClosureValue function = ((ClosureValue) func);
            return callClosure(function);
        }

        throw SyntaxException.withSyntax(func + " 不是一个函数");
    }


    private BaseValue callClosure(ClosureValue closure) {

        Scope defineScope = closure.getDefineScope();

        defineScope.initReturnSpace();

        BaseNode block = closure.getBlock();

        block.eval(defineScope);

        List<BaseValue> returnSpace = defineScope.getReturnSpace();

        return NullValue.INSTANCE;
    }

}
