package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.common.Tuple2;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DoubleValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.Arrays;
import java.util.function.BiFunction;

public class SubOperation implements BinaryOperation {

    @SuppressWarnings("unchecked")
    private static final BiFunction<RuntimeContext, Tuple2<BaseValue, BaseValue>, BaseValue>[] operations =
            new BiFunction[ValueType.values().length * ValueType.values().length];

    static {

        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.INTEGER)] =
                (ctx, tuple) -> ctx.getConfiguration().getIntegerNumberCache()
                        .getValue(((IntegerValue) tuple.v1).getValue()
                                - ((IntegerValue) tuple.v2).getValue());

        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.DOUBLE)] =
                (ctx, tuple) -> new DoubleValue(
                        ((IntegerValue) tuple.v1).getValue() - ((DoubleValue) tuple.v2).getValue());

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.INTEGER)] =
                (ctx, tuple) -> new DoubleValue(
                        ((DoubleValue) tuple.v1).getValue() - ((IntegerValue) tuple.v2).getValue());

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.DOUBLE)] =
                (ctx, tuple) -> new DoubleValue(
                        ((DoubleValue) tuple.v1).getValue() - ((DoubleValue) tuple.v2).getValue());

    }

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Environment env, RuntimeContext context,
            NodeVisitor visitor, BaseValue... params) {
        BaseValue lValue = lhs.eval(visitor);
        BaseValue rValue = rhs.eval(visitor);
        BiFunction<RuntimeContext, Tuple2<BaseValue, BaseValue>, BaseValue> operation =
                operations[IOUtil.getTypeIndex(lValue.getType(), rValue.getType())];
        if (operation != null) {
            return operation.apply(context, new Tuple2<>(lValue, rValue));
        } else {
            throw SyntaxException
                    .withSyntax("Subtraction operation is not supported for these values: "
                            + Arrays.toString(params));
        }
    }

}
