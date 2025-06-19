package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.Tuple2;
import com.kamijoucen.ruler.exception.TypeException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DoubleValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.function.BiFunction;

/**
 * 加法操作实现
 * 支持整数和浮点数的加法运算
 *
 * @author Kamijoucen
 */
public class AddOperation implements BinaryOperation {

    @SuppressWarnings("unchecked")
    private static final BiFunction<RuntimeContext, Tuple2<BaseValue, BaseValue>, BaseValue>[] operations =
            new BiFunction[ValueType.values().length * ValueType.values().length];

    static {
        // INTEGER + INTEGER
        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.INTEGER)] = (ctx, tuple) -> {
            IntegerValue val1 = (IntegerValue) tuple.v1;
            IntegerValue val2 = (IntegerValue) tuple.v2;
            long result = val1.getValue() + val2.getValue();
            // 检查溢出
            if (((val1.getValue() ^ result) & (val2.getValue() ^ result)) < 0) {
                // 发生溢出，转换为浮点数
                return new DoubleValue((double) val1.getValue() + (double) val2.getValue());
            }
            return ctx.getConfiguration().getIntegerNumberCache().getValue(result);
        };

        // INTEGER + DOUBLE
        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.DOUBLE)] = (ctx, tuple) -> {
            IntegerValue val1 = (IntegerValue) tuple.v1;
            DoubleValue val2 = (DoubleValue) tuple.v2;
            return new DoubleValue(val1.getValue() + val2.getValue());
        };

        // DOUBLE + INTEGER
        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.INTEGER)] = (ctx, tuple) -> {
            DoubleValue val1 = (DoubleValue) tuple.v1;
            IntegerValue val2 = (IntegerValue) tuple.v2;
            return new DoubleValue(val1.getValue() + val2.getValue());
        };

        // DOUBLE + DOUBLE
        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.DOUBLE)] = (ctx, tuple) -> {
            DoubleValue val1 = (DoubleValue) tuple.v1;
            DoubleValue val2 = (DoubleValue) tuple.v2;
            return new DoubleValue(val1.getValue() + val2.getValue());
        };
    }

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);

        BiFunction<RuntimeContext, Tuple2<BaseValue, BaseValue>, BaseValue> operation =
                operations[IOUtil.getTypeIndex(lValue.getType(), rValue.getType())];

        if (operation != null) {
            return operation.apply(context, new Tuple2<>(lValue, rValue));
        } else {
            throw TypeException.incompatibleTypes("+", lValue.getType(), rValue.getType(), lhs.getLocation());
        }
    }
}
