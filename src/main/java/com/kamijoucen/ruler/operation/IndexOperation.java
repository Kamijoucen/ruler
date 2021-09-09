package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.constant.NullValue;

public class IndexOperation implements Operation {

    @Override
    public BaseValue compute(RuntimeContext context, BaseValue... param) {

        BaseValue tempArray = param[0];
        BaseValue tempIndex = param[1];

        if (tempIndex.getType() != ValueType.INTEGER) {
            throw SyntaxException.withSyntax("数组的索引必须是数字");
        }

        ArrayValue array = (ArrayValue) tempArray;

        IntegerValue index = (IntegerValue) tempIndex;

        if (index.getValue() >= array.getValues().size()) {
            return NullValue.INSTANCE;
        }

        return array.getValues().get(index.getValue());
    }

}
