package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.StringValue;

public class StringAddOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Environment env, RuntimeContext context,
            NodeVisitor visitor, BaseValue... params) {
        BaseValue lValue = lhs.eval(visitor);
        BaseValue rValue = rhs.eval(visitor);
        return new StringValue(lValue.toString() + rValue.toString());
    }

}
