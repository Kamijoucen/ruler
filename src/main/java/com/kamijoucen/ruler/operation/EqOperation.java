package com.kamijoucen.ruler.operation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.Pair;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.*;

public class EqOperation implements BinaryOperation {

    private final Map<Pair<ValueType, ValueType>, BiFunction<BaseValue, BaseValue, BaseValue>> operations = new HashMap<>();

    public EqOperation(boolean strict) {
        initStrictOp();
        if (!strict) {
            initNonStrictOp();
        }
    }

    private void initStrictOp() {
        operations.put(Pair.of(ValueType.INTEGER, ValueType.INTEGER), (l, r) -> {
            IntegerValue val1 = (IntegerValue) l;
            IntegerValue val2 = (IntegerValue) r;
            return BoolValue.get(val1.getValue() == val2.getValue());
        });
        operations.put(Pair.of(ValueType.INTEGER, ValueType.DOUBLE), (l, r) -> {
            IntegerValue val1 = (IntegerValue) l;
            DoubleValue val2 = (DoubleValue) r;
            return BoolValue.get(val1.getValue() == val2.getValue());
        });
        operations.put(Pair.of(ValueType.DOUBLE, ValueType.INTEGER), (l, r) -> {
            DoubleValue val1 = (DoubleValue) l;
            IntegerValue val2 = (IntegerValue) r;
            return BoolValue.get(val1.getValue() == val2.getValue());
        });
        operations.put(Pair.of(ValueType.DOUBLE, ValueType.DOUBLE), (l, r) -> {
            DoubleValue val1 = (DoubleValue) l;
            DoubleValue val2 = (DoubleValue) r;
            return BoolValue.get(val1.getValue() == val2.getValue());
        });
        operations.put(Pair.of(ValueType.STRING, ValueType.STRING), (l, r) -> {
            return BoolValue.get(l.toString().equals(r.toString()));
        });
        operations.put(Pair.of(ValueType.NULL, ValueType.NULL), (l, r) -> {
            return BoolValue.get(true);
        });
    }

    private void initNonStrictOp() {
        operations.put(Pair.of(ValueType.STRING, ValueType.INTEGER), (l, r) -> {
            try {
                long val1 = Long.parseLong(r.toString());
                IntegerValue val2 = (IntegerValue) l;
                return BoolValue.get(val1 == val2.getValue());
            } catch (NumberFormatException e) {
                return BoolValue.get(false);
            }
        });
        operations.put(Pair.of(ValueType.INTEGER, ValueType.STRING), (l, r) -> {
            try {
                IntegerValue val1 = (IntegerValue) l;
                StringValue val2 = (StringValue) r;
                return BoolValue.get(val1.getValue() == Long.parseLong(val2.getValue()));
            } catch (NumberFormatException e) {
                return BoolValue.get(false);
            }
        });
        operations.put(Pair.of(ValueType.STRING, ValueType.DOUBLE), (l, r) -> {
            try {
                double val1 = Double.parseDouble(r.toString());
                DoubleValue val2 = (DoubleValue) l;
                return BoolValue.get(val1 == val2.getValue());
            } catch (NumberFormatException e) {
                return BoolValue.get(false);
            }
        });
        operations.put(Pair.of(ValueType.DOUBLE, ValueType.STRING), (l, r) -> {
            try {
                double val1 = Double.parseDouble(l.toString());
                StringValue val2 = (StringValue) r;
                return BoolValue.get(val1 == Double.parseDouble(val2.getValue()));
            } catch (NumberFormatException e) {
                return BoolValue.get(false);
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
                return BoolValue.get(false);
            }
            if (lValue.getType() != rValue.getType()) {
                return BoolValue.get(false);
            }
            return BoolValue.get(lValue.toString().equals(rValue.toString()));
        }
    }
}
