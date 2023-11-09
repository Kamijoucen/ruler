package com.kamijoucen.ruler.operation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.Pair;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.DoubleValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.ValueType;

public class NeOperation implements BinaryOperation {

    private final Map<Pair<ValueType, ValueType>, BiFunction<BaseValue, BaseValue, BaseValue>> operations = new HashMap<>();
    {
        operations.put(Pair.of(ValueType.INTEGER, ValueType.INTEGER),
                (l, r) -> BoolValue.get(((IntegerValue) l).getValue() != ((IntegerValue) r).getValue()));
        operations.put(Pair.of(ValueType.INTEGER, ValueType.DOUBLE),
                (l, r) -> BoolValue.get(((IntegerValue) l).getValue() != ((DoubleValue) r).getValue()));
        operations.put(Pair.of(ValueType.DOUBLE, ValueType.INTEGER),
                (l, r) -> BoolValue.get(((DoubleValue) l).getValue() != ((IntegerValue) r).getValue()));
        operations.put(Pair.of(ValueType.DOUBLE, ValueType.DOUBLE),
                (l, r) -> BoolValue.get(((DoubleValue) l).getValue() != ((DoubleValue) r).getValue()));
        operations.put(Pair.of(ValueType.STRING, ValueType.STRING),
                (l, r) -> BoolValue.get(!l.toString().equals(r.toString())));
        operations.put(Pair.of(ValueType.NULL, ValueType.NULL), (l, r) -> BoolValue.get(false));
    }

    public NeOperation(boolean strict) {
        if (!strict) {
            initNonStrictOp();
        }
    }

    private void initNonStrictOp() {
        operations.put(Pair.of(ValueType.STRING, ValueType.INTEGER), (l, r) -> {
            try {
                return BoolValue.get(Double.parseDouble(l.toString()) != ((IntegerValue) r).getValue());
            } catch (NumberFormatException e) {
                return BoolValue.get(true);
            }
        });
        operations.put(Pair.of(ValueType.STRING, ValueType.DOUBLE), (l, r) -> {
            try {
                return BoolValue.get(Double.parseDouble(l.toString()) != ((DoubleValue) r).getValue());
            } catch (NumberFormatException e) {
                return BoolValue.get(true);
            }
        });
        operations.put(Pair.of(ValueType.INTEGER, ValueType.STRING), (l, r) -> {
            try {
                return BoolValue.get(((IntegerValue) l).getValue() != Double.parseDouble(r.toString()));
            } catch (NumberFormatException e) {
                return BoolValue.get(true);
            }
        });
        operations.put(Pair.of(ValueType.DOUBLE, ValueType.STRING), (l, r) -> {
            try {
                return BoolValue.get(((DoubleValue) l).getValue() != Double.parseDouble(r.toString()));
            } catch (NumberFormatException e) {
                return BoolValue.get(true);
            }
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
            if (lValue.getType() == ValueType.NULL || rValue.getType() == ValueType.NULL) {
                return BoolValue.get(true);
            }
            if (lValue.getType() != rValue.getType()) {
                return BoolValue.get(true);
            }
            return BoolValue.get(!lValue.toString().equals(rValue.toString()));
        }
    }
}
