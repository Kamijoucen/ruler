package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.Pair;

public class GtOperation implements BinaryOperation {

    private static final Map<Pair<ValueType, ValueType>, BiFunction<BaseValue, BaseValue, BaseValue>> operations = new HashMap<>();

    static {
        operations.put(Pair.of(ValueType.INTEGER, ValueType.INTEGER),
                (l, r) -> BoolValue.get(((IntegerValue) l).getValue() >= ((IntegerValue) r).getValue()));
        operations.put(Pair.of(ValueType.INTEGER, ValueType.DOUBLE),
                (l, r) -> BoolValue.get(((IntegerValue) l).getValue() >= ((DoubleValue) r).getValue()));
        operations.put(Pair.of(ValueType.DOUBLE, ValueType.INTEGER),
                (l, r) -> BoolValue.get(((DoubleValue) l).getValue() >= ((IntegerValue) r).getValue()));
        operations.put(Pair.of(ValueType.DOUBLE, ValueType.DOUBLE),
                (l, r) -> BoolValue.get(((DoubleValue) l).getValue() >= ((DoubleValue) r).getValue()));
    }

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);
        BiFunction<BaseValue, BaseValue, BaseValue> operation = operations
                .get(Pair.of(lValue.getType(), rValue.getType()));
        if (operation != null) {
            return operation.apply(lValue, rValue);
        } else {
            throw SyntaxException.withSyntax("Unsupported operation for these types: " + Arrays.toString(params));
        }
    }
}
