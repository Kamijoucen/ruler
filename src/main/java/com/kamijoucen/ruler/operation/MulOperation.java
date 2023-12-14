package com.kamijoucen.ruler.operation;

import java.util.Arrays;
import java.util.function.BiFunction;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.Tuple2;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DoubleValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.ValueType;

public class MulOperation implements BinaryOperation {

    @SuppressWarnings("unchecked")
    private static final BiFunction<RuntimeContext, Tuple2<BaseValue, BaseValue>, BaseValue>[] operations =
            new BiFunction[ValueType.values().length * ValueType.values().length];

    static {

        operations[IOUtil.getIndex(ValueType.INTEGER, ValueType.INTEGER)] =
                (ctx, tuple) -> ctx.getConfiguration().getIntegerNumberCache()
                        .getValue(((IntegerValue) tuple.v1).getValue()
                                * ((IntegerValue) tuple.v2).getValue());

        operations[IOUtil.getIndex(ValueType.INTEGER, ValueType.DOUBLE)] =
                (ctx, tuple) -> new DoubleValue(
                        ((IntegerValue) tuple.v1).getValue() * ((DoubleValue) tuple.v2).getValue());

        operations[IOUtil.getIndex(ValueType.DOUBLE, ValueType.INTEGER)] =
                (ctx, tuple) -> new DoubleValue(
                        ((DoubleValue) tuple.v1).getValue() * ((IntegerValue) tuple.v2).getValue());

        operations[IOUtil.getIndex(ValueType.DOUBLE, ValueType.DOUBLE)] =
                (ctx, tuple) -> new DoubleValue(
                        ((DoubleValue) tuple.v1).getValue() * ((DoubleValue) tuple.v2).getValue());
    }

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);
        BiFunction<RuntimeContext, Tuple2<BaseValue, BaseValue>, BaseValue> operation =
                operations[IOUtil.getIndex(lValue.getType(), rValue.getType())];
        if (operation != null) {
            return operation.apply(context, new Tuple2<BaseValue, BaseValue>(lValue, rValue));
        } else {
            throw SyntaxException.withSyntax(
                    "Unsupported operation for these types: " + Arrays.toString(params));
        }
    }
}
