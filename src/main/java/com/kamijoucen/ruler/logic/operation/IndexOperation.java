package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.ArrayValue;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.RsonValue;
import com.kamijoucen.ruler.domain.value.StringValue;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.logic.util.NumberUtil;

public class IndexOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {

        BaseValue lVal = lhs.eval(scope, context);
        BaseValue idx = rhs.eval(scope, context);

        if (lVal.getType() == ValueType.ARRAY && idx.getType() == ValueType.INTEGER) {
            ArrayValue array = (ArrayValue) lVal;
            int index = NumberUtil.toIntIndex((IntegerValue) idx);
            // 检查数组是否越界
            if (index >= array.getValues().size()) {
                throw new IndexOutOfBoundsException("Array index out of bounds");
            }
            return array.getValues().get(index);
        } else if (lVal.getType() == ValueType.RSON && idx.getType() == ValueType.STRING) {
            RsonValue rson = (RsonValue) lVal;
            StringValue string = (StringValue) idx;
            return context.getConfiguration().getObjectAccessControlManager().accessObject(rson,
                    string.getValue(), context);
        } else if (lVal.getType() == ValueType.STRING && idx.getType() == ValueType.INTEGER) {
            StringValue str = (StringValue) lVal;
            int index = NumberUtil.toIntIndex((IntegerValue) idx);
            if (index >= str.getValue().length()) {
                throw new IndexOutOfBoundsException("String index out of bounds");
            }
            return new StringValue(String.valueOf(str.getValue().charAt(index)));
        } else {
            // TODO message fmt
            throw new SyntaxException("index operation not supported");
        }
    }

}
