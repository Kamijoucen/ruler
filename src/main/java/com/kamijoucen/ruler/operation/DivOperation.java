package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DoubleValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.Arrays;
import java.util.function.BiFunction;

public class DivOperation implements BinaryOperation {

    @SuppressWarnings("unchecked")
    private static final BiFunction<BaseValue, BaseValue, BaseValue>[] operations =
            new BiFunction[ValueType.values().length * ValueType.values().length];

    static {

        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.INTEGER)] =
                (l, r) -> new DoubleValue(
                        (double) ((IntegerValue) l).getValue() / ((IntegerValue) r).getValue());

        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.DOUBLE)] = (l,
                r) -> new DoubleValue(((IntegerValue) l).getValue() / ((DoubleValue) r).getValue());

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.INTEGER)] = (l,
                r) -> new DoubleValue(((DoubleValue) l).getValue() / ((IntegerValue) r).getValue());

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.DOUBLE)] = (l,
                r) -> new DoubleValue(((DoubleValue) l).getValue() / ((DoubleValue) r).getValue());
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
            throw SyntaxException.withSyntax("该值不支持做除法:" + Arrays.toString(params));
        }
    }
}
