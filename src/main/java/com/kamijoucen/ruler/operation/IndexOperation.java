package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.RsonValue;
import com.kamijoucen.ruler.value.StringValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.constant.NullValue;

public class IndexOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {

        BaseValue lval = lhs.eval(scope, context);
        BaseValue idx = rhs.eval(scope, context);

        if (lval.getType() == ValueType.ARRAY && idx.getType() == ValueType.INTEGER) {
            ArrayValue array = (ArrayValue) lval;
            IntegerValue index = (IntegerValue) idx;
            // 检查数组是否越界
            if (index.getValue() >= array.getValues().size()) {
                throw new IndexOutOfBoundsException("Array index out of bounds");
            }
            return array.getValues().get((int) index.getValue());
        } else if (lval.getType() == ValueType.RSON && idx.getType() == ValueType.STRING) {

            RsonValue rson = (RsonValue) lval;
            StringValue string = (StringValue) idx;

            BaseValue callValue = rson.getField(string.getValue());
            if (callValue == null) {
                callValue = context.getConfiguration().getRClassManager().getClassValue(ValueType.RSON)
                        .getProperty(string.getValue());           
                if (callValue == null) {
                    callValue = NullValue.INSTANCE;
                }
            }
            return callValue;
        } else {
            throw SyntaxException.withSyntax("Index operations can only be used for arrays and RSON.");
        }
    }

}
