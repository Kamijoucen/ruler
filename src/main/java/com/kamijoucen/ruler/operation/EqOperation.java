package com.kamijoucen.ruler.operation;

import java.util.function.BiFunction;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.*;

public class EqOperation implements BinaryOperation {

    @SuppressWarnings("unchecked")
    private final BiFunction<BaseValue, BaseValue, BaseValue>[] operations =
            new BiFunction[ValueType.values().length * ValueType.values().length];

    public EqOperation(boolean strict) {
        initStrictOp();
        if (!strict) {
            initNonStrictOp();
        }
    }

    private void initStrictOp() {

        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.INTEGER)] = (l, r) -> {
            IntegerValue val1 = (IntegerValue) l;
            IntegerValue val2 = (IntegerValue) r;
            return BoolValue.get(val1.getValue() == val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.DOUBLE)] = (l, r) -> {
            IntegerValue val1 = (IntegerValue) l;
            DoubleValue val2 = (DoubleValue) r;
            return BoolValue.get(val1.getValue() == val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.INTEGER)] = (l, r) -> {
            DoubleValue val1 = (DoubleValue) l;
            IntegerValue val2 = (IntegerValue) r;
            return BoolValue.get(val1.getValue() == val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.DOUBLE)] = (l, r) -> {
            DoubleValue val1 = (DoubleValue) l;
            DoubleValue val2 = (DoubleValue) r;
            return BoolValue.get(val1.getValue() == val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.BOOL, ValueType.BOOL)] = (l, r) -> {
            BoolValue val1 = (BoolValue) l;
            BoolValue val2 = (BoolValue) r;
            return BoolValue.get(val1.getValue() == val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.STRING, ValueType.STRING)] = (l, r) -> {
            StringValue val1 = (StringValue) l;
            StringValue val2 = (StringValue) r;
            return BoolValue.get(val1.getValue().equals(val2.getValue()));
        };

        operations[IOUtil.getTypeIndex(ValueType.NULL, ValueType.NULL)] = (l, r) -> BoolValue.get(true);
    }

    private void initNonStrictOp() {

        operations[IOUtil.getTypeIndex(ValueType.STRING, ValueType.INTEGER)] = (l, r) -> {
            try {
                IntegerValue val2 = (IntegerValue) l;
                long val1 = Long.parseLong(r.toString());
                return BoolValue.get(val1 == val2.getValue());
            } catch (NumberFormatException e) {
                return BoolValue.get(false);
            }
        };

        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.STRING)] = (l, r) -> {
            try {
                long val1 = Long.parseLong(l.toString());
                StringValue val2 = (StringValue) r;
                return BoolValue.get(val1 == Long.parseLong(val2.getValue()));
            } catch (NumberFormatException e) {
                return BoolValue.get(false);
            }
        };

        operations[IOUtil.getTypeIndex(ValueType.STRING, ValueType.DOUBLE)] = (l, r) -> {
            try {
                DoubleValue val2 = (DoubleValue) l;
                double val1 = Double.parseDouble(r.toString());
                return BoolValue.get(val1 == val2.getValue());
            } catch (NumberFormatException e) {
                return BoolValue.get(false);
            }
        };

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.STRING)] = (l, r) -> {
            try {
                double val1 = Double.parseDouble(l.toString());
                StringValue val2 = (StringValue) r;
                return BoolValue.get(val1 == Double.parseDouble(val2.getValue()));
            } catch (NumberFormatException e) {
                return BoolValue.get(false);
            }
        };
    }

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);

        BiFunction<BaseValue, BaseValue, BaseValue> operation = operations[IOUtil.getTypeIndex(lValue.getType(), rValue.getType())];
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
