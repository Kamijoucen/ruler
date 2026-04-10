package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.common.Tuple2;
import com.kamijoucen.ruler.domain.exception.IllegalOperationException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.logic.util.IOUtil;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.ValueType;

import java.util.function.BiFunction;

public class AddOperation implements BinaryOperation {

    @SuppressWarnings("unchecked")
    private static final BiFunction<RuntimeContext, Tuple2<BaseValue, BaseValue>, BaseValue>[] operations =
            new BiFunction[ValueType.values().length * ValueType.values().length];

    static {
        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.INTEGER)] = (ctx, tuple) -> {
            IntegerValue val1 = (IntegerValue) tuple.v1;
            IntegerValue val2 = (IntegerValue) tuple.v2;
            return ctx.getConfiguration().getIntegerNumberCache()
                    .getValue(val1.getValue() + val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.DOUBLE)] = (ctx, tuple) -> {
            IntegerValue val1 = (IntegerValue) tuple.v1;
            DoubleValue val2 = (DoubleValue) tuple.v2;
            return new DoubleValue(val1.getValue() + val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.INTEGER)] = (ctx, tuple) -> {
            DoubleValue val1 = (DoubleValue) tuple.v1;
            IntegerValue val2 = (IntegerValue) tuple.v2;
            return new DoubleValue(val1.getValue() + val2.getValue());
        };

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
            throw new IllegalOperationException(
                    "'add' operation not supported for: " + lValue.getType() + " and " + rValue.getType());
        }
    }
}
