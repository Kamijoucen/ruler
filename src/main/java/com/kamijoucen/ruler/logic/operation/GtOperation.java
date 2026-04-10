package com.kamijoucen.ruler.logic.operation;

import java.util.Arrays;
import java.util.function.BiFunction;
import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.logic.util.IOUtil;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.BoolValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.ValueType;

public class GtOperation implements BinaryOperation {

    @SuppressWarnings("unchecked")
    private static final BiFunction<BaseValue, BaseValue, BaseValue>[] operations =
            new BiFunction[ValueType.values().length * ValueType.values().length];

    static {

        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.INTEGER)] = (l, r) -> {
            IntegerValue val1 = (IntegerValue) l;
            IntegerValue val2 = (IntegerValue) r;
            return BoolValue.get(val1.getValue() > val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.DOUBLE)] = (l, r) -> {
            IntegerValue val1 = (IntegerValue) l;
            DoubleValue val2 = (DoubleValue) r;
            return BoolValue.get(val1.getValue() > val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.INTEGER)] = (l, r) -> {
            DoubleValue val1 = (DoubleValue) l;
            IntegerValue val2 = (IntegerValue) r;
            return BoolValue.get(val1.getValue() > val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.DOUBLE)] = (l, r) -> {
            DoubleValue val1 = (DoubleValue) l;
            DoubleValue val2 = (DoubleValue) r;
            return BoolValue.get(val1.getValue() > val2.getValue());
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
            throw SyntaxException.withSyntax(
                    "Unsupported operation for these types: " + Arrays.toString(params));
        }
    }
}
