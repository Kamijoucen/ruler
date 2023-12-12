package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.Tuple2;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.*;

import java.util.Arrays;
import java.util.function.BiFunction;

public class AddOperation implements BinaryOperation {

    @SuppressWarnings("unchecked")
    private static final BiFunction<RuntimeContext, Tuple2<BaseValue, BaseValue>, BaseValue>[] operations = new BiFunction[ValueType
            .values().length * ValueType.values().length];

    static {
        for (ValueType type1 : ValueType.values()) {
            for (ValueType type2 : ValueType.values()) {
                int index = type1.ordinal() * ValueType.values().length + type2.ordinal();
                if (type1 == ValueType.INTEGER && type2 == ValueType.INTEGER) {
                    operations[index] = (ctx, tuple) -> {
                        IntegerValue val1 = (IntegerValue) tuple.v1;
                        IntegerValue val2 = (IntegerValue) tuple.v2;
                        return ctx.getConfiguration().getIntegerNumberCache()
                                .getValue(val1.getValue() + val2.getValue());
                    };
                } else if (type1 == ValueType.INTEGER && type2 == ValueType.DOUBLE) {
                    operations[index] = (ctx, tuple) -> {
                        IntegerValue val1 = (IntegerValue) tuple.v1;
                        DoubleValue val2 = (DoubleValue) tuple.v2;
                        return new DoubleValue(val1.getValue() + val2.getValue());
                    };
                } else if (type1 == ValueType.DOUBLE && type2 == ValueType.INTEGER) {
                    operations[index] = (ctx, tuple) -> {
                        DoubleValue val1 = (DoubleValue) tuple.v1;
                        IntegerValue val2 = (IntegerValue) tuple.v2;
                        return new DoubleValue(val1.getValue() + val2.getValue());
                    };
                } else if (type1 == ValueType.DOUBLE && type2 == ValueType.DOUBLE) {
                    operations[index] = (ctx, tuple) -> {
                        DoubleValue val1 = (DoubleValue) tuple.v1;
                        DoubleValue val2 = (DoubleValue) tuple.v2;
                        return new DoubleValue(val1.getValue() + val2.getValue());
                    };
                } else if (type1 == ValueType.STRING && type2 == ValueType.STRING) {
                    operations[index] = (ctx, tuple) -> {
                        StringValue val1 = (StringValue) tuple.v1;
                        StringValue val2 = (StringValue) tuple.v2;
                        return new StringValue(val1.getValue() + val2.getValue());
                    };
                } else {
                    operations[index] = null;
                }
            }
        }
    }

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);
        int index = lValue.getType().ordinal() * ValueType.values().length + rValue.getType().ordinal();
        BiFunction<RuntimeContext, Tuple2<BaseValue, BaseValue>, BaseValue> operation = operations[index];
        if (operation != null) {
            return operation.apply(context, new Tuple2<BaseValue, BaseValue>(lValue, rValue));
        } else {
            throw SyntaxException
                    .withSyntax("The value is not supported for the 'add' operation:" + Arrays.toString(params));
        }
    }
}
