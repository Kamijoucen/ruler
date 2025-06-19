package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.exception.RulerRuntimeException;
import com.kamijoucen.ruler.exception.TypeException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.*;

/**
 * 除法操作
 * 支持整数和浮点数的除法运算
 *
 * @author Kamijoucen
 */
public class DivOperation implements BinaryOperation {

    @Override
    public BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context,
            BaseValue... params) {
        BaseValue lValue = lhs.eval(scope, context);
        BaseValue rValue = rhs.eval(scope, context);

        // 检查除数是否为0
        if (isZero(rValue)) {
            throw new RulerRuntimeException("除数不能为0", lhs.getLocation());
        }

        ValueType lType = lValue.getType();
        ValueType rType = rValue.getType();

        // 整数除法
        if (lType == ValueType.INTEGER && rType == ValueType.INTEGER) {
            long dividend = ((IntegerValue) lValue).getValue();
            long divisor = ((IntegerValue) rValue).getValue();

            // 如果能整除，返回整数
            if (dividend % divisor == 0) {
                return context.getConfiguration().getIntegerNumberCache()
                        .getValue(dividend / divisor);
            } else {
                // 否则返回浮点数
                return new DoubleValue((double) dividend / divisor);
            }
        }

        // 浮点数除法
        if ((lType == ValueType.INTEGER || lType == ValueType.DOUBLE) &&
            (rType == ValueType.INTEGER || rType == ValueType.DOUBLE)) {

            double dividend = toDouble(lValue);
            double divisor = toDouble(rValue);
            return new DoubleValue(dividend / divisor);
        }

                // 不支持的类型
        throw TypeException.unsupportedOperation("/",
                lType == rType ? lType : ValueType.UN_KNOWN, lhs.getLocation());
    }

    private boolean isZero(BaseValue value) {
        if (value.getType() == ValueType.INTEGER) {
            return ((IntegerValue) value).getValue() == 0;
        } else if (value.getType() == ValueType.DOUBLE) {
            return ((DoubleValue) value).getValue() == 0.0;
        }
        return false;
    }

    private double toDouble(BaseValue value) {
        if (value.getType() == ValueType.INTEGER) {
            return ((IntegerValue) value).getValue();
        } else {
            return ((DoubleValue) value).getValue();
        }
    }
}
