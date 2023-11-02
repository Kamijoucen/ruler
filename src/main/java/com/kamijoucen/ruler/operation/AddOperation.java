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

public class AddOperation implements BinaryOperation {

    private static final Map<Pair<ValueType, ValueType>, BiFunction<BaseValue, BaseValue, BaseValue>> operations = new HashMap<>();

    static {

        operations.put(Pair.of(ValueType.INTEGER, ValueType.INTEGER), (l, r) -> {
            IntegerValue val1 = (IntegerValue) l;
            IntegerValue val2 = (IntegerValue) r;
            return new IntegerValue(val1.getValue() + val2.getValue());
        });

        operations.put(Pair.of(ValueType.INTEGER, ValueType.DOUBLE), (l, r) -> {
            IntegerValue val1 = (IntegerValue) l;
            DoubleValue val2 = (DoubleValue) r;
            return new DoubleValue(val1.getValue() + val2.getValue());
        });

        operations.put(Pair.of(ValueType.DOUBLE, ValueType.INTEGER), (l, r) -> {
            DoubleValue val1 = (DoubleValue) l;
            IntegerValue val2 = (IntegerValue) r;
            return new DoubleValue(val1.getValue() + val2.getValue());
        });

        operations.put(Pair.of(ValueType.DOUBLE, ValueType.DOUBLE), (l, r) -> {
            DoubleValue val1 = (DoubleValue) l;
            DoubleValue val2 = (DoubleValue) r;
            return new DoubleValue(val1.getValue() + val2.getValue());
        });

        operations.put(Pair.of(ValueType.STRING, ValueType.STRING), (l, r) -> {
            StringValue val1 = (StringValue) l;
            StringValue val2 = (StringValue) r;
            return new StringValue(val1.getValue() + val2.getValue());
        });

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
            throw SyntaxException
                    .withSyntax("The value is not supported for the 'add' operation:" + Arrays.toString(params));
        }
    }
}
