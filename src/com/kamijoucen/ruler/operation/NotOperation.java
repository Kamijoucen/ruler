package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;

public class NotOperation implements LogicOperation {
    @Override
    public BaseValue compute(Scope scope, BaseNode... nodes) {

        BaseNode exp = nodes[0];

        BaseValue tempExpVal = exp.eval(scope);

        if (tempExpVal.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("该值不支持!:" + tempExpVal);
        }
        BoolValue expVal = (BoolValue) tempExpVal;
        return BoolValue.get(!expVal.getValue());
    }
}
