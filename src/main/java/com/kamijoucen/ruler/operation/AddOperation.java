package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.Tuple2;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.Pair;

public class AddOperation implements BinaryOperation {

    private static final Map<Pair<ValueType, ValueType>, BiFunction<RuntimeContext, Tuple2<BaseValue, BaseValue>, BaseValue>> operations = new HashMap<>();

    static {

        operations.put(Pair.of(ValueType.INTEGER, ValueType.INTEGER), (ctx, tuple) -> {
            IntegerValue val1 = (IntegerValue) tuple.v1;
            IntegerValue val2 = (IntegerValue) tuple.v2;
            return ctx.getConfiguration().getIntegerNumberCache().getValue(val1.getValue() + val2.getValue());
        });

        operations.put(Pair.of(ValueType.INTEGER, ValueType.DOUBLE), (ctx, tuple) -> {
            IntegerValue val1 = (IntegerValue) tuple.v1;
            DoubleValue val2 = (DoubleValue) tuple.v2;
            return new DoubleValue(val1.getValue() + val2.getValue());
        });

        operations.put(Pair.of(ValueType.DOUBLE, ValueType.INTEGER), (ctx, tuple) -> {
            DoubleValue val1 = (DoubleValue) tuple.v1;
            IntegerValue val2 = (IntegerValue) tuple.v2;
            return new DoubleValue(val1.getValue() + val2.getValue());
        });

        operations.put(Pair.of(ValueType.DOUBLE, ValueType.DOUBLE), (ctx, tuple) -> {
            DoubleValue val1 = (DoubleValue) tuple.v1;
            DoubleValue val2 = (DoubleValue) tuple.v2;
            return new DoubleValue(val1.getValue() + val2.getValue());
        });

        operations.put(Pair.of(ValueType.STRING, ValueType.STRING), (ctx, tuple) -> {
            StringValue val1 = (StringValue) tuple.v1;
            StringValue val2 = (StringValue) tuple.v2;
            return new StringValue(val1.getValue() + val2.getValue());
        });

    }

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);
        BiFunction<RuntimeContext, Tuple2<BaseValue, BaseValue>, BaseValue> operation = operations
                .get(Pair.of(lValue.getType(), rValue.getType()));
        if (operation != null) {
            return operation.apply(context, new Tuple2<BaseValue, BaseValue>(lValue, rValue));
        } else {
            throw SyntaxException
                    .withSyntax("The value is not supported for the 'add' operation:" + Arrays.toString(params));
        }
    }
}
