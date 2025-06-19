package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.exception.RulerRuntimeException;
import com.kamijoucen.ruler.exception.TypeException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.RsonValue;
import com.kamijoucen.ruler.value.StringValue;
import com.kamijoucen.ruler.value.ValueType;

/**
 * 索引操作实现
 * 支持数组、字符串、对象的索引访问
 *
 * @author Kamijoucen
 */
public class IndexOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {

        BaseValue lVal = lhs.eval(scope, context);
        BaseValue idx = rhs.eval(scope, context);

        if (lVal.getType() == ValueType.ARRAY && idx.getType() == ValueType.INTEGER) {
            // 数组索引访问
            ArrayValue array = (ArrayValue) lVal;
            IntegerValue index = (IntegerValue) idx;
            long indexValue = index.getValue();

            // 检查索引是否为负数
            if (indexValue < 0) {
                throw RulerRuntimeException.indexOutOfBounds((int) indexValue, array.getValues().size(),
                        rhs.getLocation());
            }

            // 检查数组是否越界
            if (indexValue >= array.getValues().size()) {
                throw RulerRuntimeException.indexOutOfBounds((int) indexValue, array.getValues().size(),
                        rhs.getLocation());
            }

            return array.getValues().get((int) indexValue);

        } else if (lVal.getType() == ValueType.RSON && idx.getType() == ValueType.STRING) {
            // 对象属性访问
            RsonValue rson = (RsonValue) lVal;
            StringValue string = (StringValue) idx;
            return context.getConfiguration().getObjectAccessControlManager().accessObject(rson,
                    string.getValue(), context);

        } else if (lVal.getType() == ValueType.STRING && idx.getType() == ValueType.INTEGER) {
            // 字符串索引访问
            StringValue str = (StringValue) lVal;
            IntegerValue index = (IntegerValue) idx;
            long indexValue = index.getValue();

            // 检查索引是否为负数
            if (indexValue < 0) {
                throw RulerRuntimeException.indexOutOfBounds((int) indexValue, str.getValue().length(),
                        rhs.getLocation());
            }

            // 检查字符串是否越界
            if (indexValue >= str.getValue().length()) {
                throw RulerRuntimeException.indexOutOfBounds((int) indexValue, str.getValue().length(),
                        rhs.getLocation());
            }

            return new StringValue(String.valueOf(str.getValue().charAt((int) indexValue)));

        } else {
            // 不支持的索引操作
            throw TypeException.notIndexable(lVal.getType(), lhs.getLocation());
        }
    }
}
