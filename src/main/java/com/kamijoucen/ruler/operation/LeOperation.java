package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.*;

import java.util.Arrays;
import java.util.function.BiFunction;

public class LeOperation implements BinaryOperation {

    @SuppressWarnings("unchecked")
    private static final BiFunction<BaseValue, BaseValue, BaseValue>[] operations =
            new BiFunction[ValueType.values().length * ValueType.values().length];


    static {
        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.INTEGER)] = (l, r) -> {
            IntegerValue val1 = (IntegerValue) l;
            IntegerValue val2 = (IntegerValue) r;
            return BoolValue.get(val1.getValue() <= val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.INTEGER, ValueType.DOUBLE)] = (l, r) -> {
            IntegerValue val1 = (IntegerValue) l;
            DoubleValue val2 = (DoubleValue) r;
            return BoolValue.get(val1.getValue() <= val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.INTEGER)] = (l, r) -> {
            DoubleValue val1 = (DoubleValue) l;
            IntegerValue val2 = (IntegerValue) r;
            return BoolValue.get(val1.getValue() <= val2.getValue());
        };

        operations[IOUtil.getTypeIndex(ValueType.DOUBLE, ValueType.DOUBLE)] = (l, r) -> {
            DoubleValue val1 = (DoubleValue) l;
            DoubleValue val2 = (DoubleValue) r;
            return BoolValue.get(val1.getValue() <= val2.getValue());
        };
    }

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Environment env, RuntimeContext context,
            NodeVisitor visitor, BaseValue... params) {
        BaseValue lValue = lhs.eval(visitor);
        BaseValue rValue = rhs.eval(visitor);
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
