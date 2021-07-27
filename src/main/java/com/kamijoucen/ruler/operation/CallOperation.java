package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.NameNode;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.value.*;
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

            return callClosure(function, (BaseValue[]) funcParam);
        }

        throw SyntaxException.withSyntax(func + " 不是一个函数");
    }


    private BaseValue callClosure(ClosureValue closure, BaseValue[] funcParam) {

        Scope defineScope = new Scope("closure", closure.getDefineScope());

        List<BaseNode> defineParam = closure.getParam();
        for (int i = 0; i < defineParam.size(); i++) {
            NameNode name = (NameNode) defineParam.get(i);
            defineScope.putLocal(name.name.name, funcParam[i]);
        }

        defineScope.initReturnSpace();

        BaseNode block = closure.getBlock();

        block.eval(defineScope);

        List<BaseValue> returnValues = defineScope.getReturnSpace();

        if (returnValues == null || returnValues.size() == 0) {
            return NullValue.INSTANCE;
        }

        if (returnValues.size() == 1) {
            return returnValues.get(0);
        }

        return new ArrayValue(returnValues);
    }

}
