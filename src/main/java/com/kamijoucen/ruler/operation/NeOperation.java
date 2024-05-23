package com.kamijoucen.ruler.operation;

import java.util.function.BiFunction;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.DoubleValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.StringValue;
import com.kamijoucen.ruler.value.ValueType;

public class NeOperation implements BinaryOperation {

    @SuppressWarnings("unchecked")
    private final BiFunction<BaseValue, BaseValue, BaseValue>[] operations =
            new BiFunction[ValueType.values().length * ValueType.values().length];

    public NeOperation(boolean strict) {
        initStrictOp();
        if (!strict) {
            initNonStrictOp();
        }
    }

    private void initStrictOp() {

        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.INTEGER)] = (l, r) -> {
            IntegerValue val1 = (IntegerValue) l;
            IntegerValue val2 = (IntegerValue) r;
            return BoolValue.get(val1.getValue() != val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.DOUBLE)] = (l, r) -> {
            IntegerValue val1 = (IntegerValue) l;
            DoubleValue val2 = (DoubleValue) r;
            return BoolValue.get(val1.getValue() != val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.INTEGER)] = (l, r) -> {
            DoubleValue val1 = (DoubleValue) l;
            IntegerValue val2 = (IntegerValue) r;
            return BoolValue.get(val1.getValue() != val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.DOUBLE)] = (l, r) -> {
            DoubleValue val1 = (DoubleValue) l;
            DoubleValue val2 = (DoubleValue) r;
            return BoolValue.get(val1.getValue() != val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.BOOL, ValueType.BOOL)] = (l, r) -> {
            BoolValue val1 = (BoolValue) l;
            BoolValue val2 = (BoolValue) r;
            return BoolValue.get(val1.getValue() != val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.STRING, ValueType.STRING)] = (l, r) -> {
            StringValue val1 = (StringValue) l;
            StringValue val2 = (StringValue) r;
            return BoolValue.get(!val1.getValue().equals(val2.getValue()));
        };

        operations[IOUtil.getTypeIndex(ValueType.NULL, ValueType.NULL)] = (l, r) -> {
            return BoolValue.get(false);
        };
    }

    private void initNonStrictOp() {

        operations[IOUtil.getTypeIndex(ValueType.STRING, ValueType.INTEGER)] = (l, r) -> {
            try {
                return BoolValue
                        .get(Double.parseDouble(l.toString()) != ((IntegerValue) r).getValue());
            } catch (NumberFormatException e) {
                return BoolValue.get(true);
            }
        };

        operations[IOUtil.getTypeIndex(ValueType.STRING, ValueType.DOUBLE)] = (l, r) -> {
            try {
                return BoolValue
                        .get(Double.parseDouble(l.toString()) != ((DoubleValue) r).getValue());
            } catch (NumberFormatException e) {
                return BoolValue.get(true);
            }
        };

        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.STRING)] = (l, r) -> {
            try {
                return BoolValue
                        .get(((IntegerValue) l).getValue() != Double.parseDouble(r.toString()));
            } catch (NumberFormatException e) {
                return BoolValue.get(true);
            }
        };

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.STRING)] = (l, r) -> {
            try {
                return BoolValue
                        .get(((DoubleValue) l).getValue() != Double.parseDouble(r.toString()));
            } catch (NumberFormatException e) {
                return BoolValue.get(true);
            }
        };

    }

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);
        BiFunction<BaseValue, BaseValue, BaseValue> operation =
                operations[IOUtil.getTypeIndex(lValue.getType(), rValue.getType())];
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
