package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RulerFunction;
import com.kamijoucen.ruler.util.Assert;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.FunctionValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.Arrays;

public class CallOperation implements Operation {

    @Override
    public BaseValue compute(BaseValue... param) {

        BaseValue name = param[1];

        Object[] funcParam = Arrays.copyOfRange(param, 1, param.length);

        if (name.getType() == ValueType.FUNCTION) {
            RulerFunction function = ((FunctionValue) name).getValue();
            return (BaseValue) function.call(funcParam);
        }

        if (name.getType() == ValueType.CLOSURE) {
            throw Assert.todo("闭包暂未实现");
        }
        throw SyntaxException.withSyntax(name + " 不是一个函数");
    }
}
