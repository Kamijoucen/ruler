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

public class IndexOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {

        BaseValue lVal = lhs.eval(scope, context);
        BaseValue idx = rhs.eval(scope, context);

        if (lVal.getType() == ValueType.ARRAY && idx.getType() == ValueType.INTEGER) {
            ArrayValue array = (ArrayValue) lVal;
            IntegerValue index = (IntegerValue) idx;
            // 检查数组是否越界
            if (index.getValue() >= array.getValues().size()) {
                throw new IndexOutOfBoundsException("Array index out of bounds");
            }
            return array.getValues().get((int) index.getValue());
        } else if (lVal.getType() == ValueType.RSON && idx.getType() == ValueType.STRING) {
            RsonValue rson = (RsonValue) lVal;
            StringValue string = (StringValue) idx;
            return context.getConfiguration().getObjectAccessControlManager().accessObject(rson,
                    string.getValue(), context);
        } else if (lVal.getType() == ValueType.STRING && idx.getType() == ValueType.INTEGER) {
            StringValue str = (StringValue) lVal;
            IntegerValue index = (IntegerValue) idx;
            if (index.getValue() >= str.getValue().length()) {
                throw new IndexOutOfBoundsException("String index out of bounds");
            }
            return new StringValue(String.valueOf(str.getValue().charAt((int) index.getValue())));
        } else {
            // TODO message fmt
            throw new SyntaxException("Index operation not supported");
        }
    }

}
