package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DoubleValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class UnarySubOperation implements BinaryOperation {

    private static final Map<ValueType, Function<BaseValue, BaseValue>> operations = new HashMap<>();

    static {
        operations.put(ValueType.INTEGER,
                val -> new IntegerValue(-((IntegerValue) val).getValue()));
        operations.put(ValueType.DOUBLE,
                val -> new DoubleValue(-((DoubleValue) val).getValue()));
    }

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Environment env, RuntimeContext context, BaseValue... params) {
        BaseValue value = params[0];
        Function<BaseValue, BaseValue> operation = operations.get(value.getType());
        if (operation != null) {
            return operation.apply(value);
        } else {
            throw new RuntimeException(
                    "Negation operation is not supported for this value: " + Arrays.toString(params));
        }
    }
}
