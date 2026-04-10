package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.ValueType;

        import com.kamijoucen.ruler.domain.exception.IllegalOperationException;

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
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params) {
        BaseValue value = params[0];
        Function<BaseValue, BaseValue> operation = operations.get(value.getType());
        if (operation != null) {
            return operation.apply(value);
        } else {
            throw new IllegalOperationException(
                    "Negation operation is not supported for this value: " + Arrays.toString(params));
        }
    }
}
