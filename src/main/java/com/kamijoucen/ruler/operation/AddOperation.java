package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.Tuple2;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.*;

import java.util.Arrays;
import java.util.function.BiFunction;

public class AddOperation implements BinaryOperation {

    @SuppressWarnings("unchecked")
    private static final BiFunction<RuntimeContext, Tuple2<BaseValue, BaseValue>, BaseValue>[] operations =
            new BiFunction[ValueType.values().length * ValueType.values().length];

    static {
        operations[IOUtil.getIndex(ValueType.INTEGER, ValueType.INTEGER)] = (ctx, tuple) -> {
            IntegerValue val1 = (IntegerValue) tuple.v1;
            IntegerValue val2 = (IntegerValue) tuple.v2;
            return ctx.getConfiguration().getIntegerNumberCache()
                    .getValue(val1.getValue() + val2.getValue());
        };

        operations[IOUtil.getIndex(ValueType.INTEGER, ValueType.DOUBLE)] = (ctx, tuple) -> {
            IntegerValue val1 = (IntegerValue) tuple.v1;
            DoubleValue val2 = (DoubleValue) tuple.v2;
            return new DoubleValue(val1.getValue() + val2.getValue());
        };

        operations[IOUtil.getIndex(ValueType.DOUBLE, ValueType.INTEGER)] = (ctx, tuple) -> {
            DoubleValue val1 = (DoubleValue) tuple.v1;
            IntegerValue val2 = (IntegerValue) tuple.v2;
            return new DoubleValue(val1.getValue() + val2.getValue());
        };

        operations[IOUtil.getIndex(ValueType.DOUBLE, ValueType.DOUBLE)] = (ctx, tuple) -> {
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
                operations[IOUtil.getIndex(lValue.getType(), rValue.getType())];
        if (operation != null) {
            return operation.apply(context, new Tuple2<BaseValue, BaseValue>(lValue, rValue));
        } else {
            throw SyntaxException.withSyntax("The value is not supported for the 'add' operation:"
                    + Arrays.toString(params));
        }
    }
}
