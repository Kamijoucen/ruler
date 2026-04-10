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

public class SubOperation implements BinaryOperation {

    @SuppressWarnings("unchecked")
    private static final BiFunction<RuntimeContext, Tuple2<BaseValue, BaseValue>, BaseValue>[] operations =
            new BiFunction[ValueType.values().length * ValueType.values().length];

    static {

        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.INTEGER)] = (ctx, tuple) -> ctx.getConfiguration().getIntegerNumberCache().getValue(
                ((IntegerValue) tuple.v1).getValue() - ((IntegerValue) tuple.v2).getValue());

        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.DOUBLE)] =
                (ctx, tuple) -> new DoubleValue(
                        ((IntegerValue) tuple.v1).getValue() - ((DoubleValue) tuple.v2).getValue());

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.INTEGER)] =
                (ctx, tuple) -> new DoubleValue(
                        ((DoubleValue) tuple.v1).getValue() - ((IntegerValue) tuple.v2).getValue());

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.DOUBLE)] =
                (ctx, tuple) -> new DoubleValue(
                        ((DoubleValue) tuple.v1).getValue() - ((DoubleValue) tuple.v2).getValue());

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
                    "'sub' operation not supported for: " + lValue.getType() + " and " + rValue.getType());
        }
    }

}
