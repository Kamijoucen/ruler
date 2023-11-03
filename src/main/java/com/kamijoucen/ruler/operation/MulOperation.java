package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.Tuple2;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DoubleValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.Pair;

public class MulOperation implements BinaryOperation {

    private static final Map<Pair<ValueType, ValueType>, BiFunction<RuntimeContext, Tuple2<BaseValue, BaseValue>, BaseValue>> operations = new HashMap<>();

    static {
        operations.put(Pair.of(ValueType.INTEGER, ValueType.INTEGER),
                (ctx, tuple) -> ctx.getConfiguration().getIntegerNumberCache()
                        .getValue(((IntegerValue) tuple.v1).getValue() * ((IntegerValue) tuple.v2).getValue()));
        operations.put(Pair.of(ValueType.INTEGER, ValueType.DOUBLE),
                (ctx, tuple) -> new DoubleValue(
                        ((IntegerValue) tuple.v1).getValue() * ((DoubleValue) tuple.v2).getValue()));
        operations.put(Pair.of(ValueType.DOUBLE, ValueType.INTEGER),
                (ctx, tuple) -> new DoubleValue(
                        ((DoubleValue) tuple.v1).getValue() * ((IntegerValue) tuple.v2).getValue()));
        operations.put(Pair.of(ValueType.DOUBLE, ValueType.DOUBLE),
                (ctx, tuple) -> new DoubleValue(
                        ((DoubleValue) tuple.v1).getValue() * ((DoubleValue) tuple.v2).getValue()));
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
            throw SyntaxException.withSyntax("Unsupported operation for these types: " + Arrays.toString(params));
        }
    }
}
