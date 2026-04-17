package com.kamijoucen.ruler.logic.util;

import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.DoubleValue;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.ValueType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class NumberUtil {

    public static final int DEFAULT_SCALE = 16;
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    private NumberUtil() {
    }

    public static BigDecimal toBigDecimal(IntegerValue value) {
        return new BigDecimal(value.getValue());
    }

    public static IntegerValue add(IntegerValue a, IntegerValue b) {
        return new IntegerValue(a.getValue().add(b.getValue()));
    }

    public static IntegerValue sub(IntegerValue a, IntegerValue b) {
        return new IntegerValue(a.getValue().subtract(b.getValue()));
    }

    public static IntegerValue mul(IntegerValue a, IntegerValue b) {
        return new IntegerValue(a.getValue().multiply(b.getValue()));
    }

    public static DoubleValue div(IntegerValue a, IntegerValue b) {
        return div(toBigDecimal(a), new BigDecimal(b.getValue()));
    }

    public static DoubleValue add(DoubleValue a, DoubleValue b) {
        return new DoubleValue(a.getValue().add(b.getValue()));
    }

    public static DoubleValue sub(DoubleValue a, DoubleValue b) {
        return new DoubleValue(a.getValue().subtract(b.getValue()));
    }

    public static DoubleValue mul(DoubleValue a, DoubleValue b) {
        return new DoubleValue(a.getValue().multiply(b.getValue()));
    }

    public static DoubleValue div(DoubleValue a, DoubleValue b) {
        return div(a.getValue(), b.getValue());
    }

    public static DoubleValue div(BigDecimal a, BigDecimal b) {
        return new DoubleValue(a.divide(b, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE));
    }

    public static IntegerValue negate(IntegerValue value) {
        return new IntegerValue(value.getValue().negate());
    }

    public static DoubleValue negate(DoubleValue value) {
        return new DoubleValue(value.getValue().negate());
    }

    public static int compareNumbers(BaseValue a, BaseValue b) {
        if (a.getType() == ValueType.INTEGER && b.getType() == ValueType.INTEGER) {
            return ((IntegerValue) a).getValue().compareTo(((IntegerValue) b).getValue());
        }
        BigDecimal left = toBigDecimal(a);
        BigDecimal right = toBigDecimal(b);
        return left.compareTo(right);
    }

    public static BigDecimal toBigDecimal(BaseValue value) {
        if (value.getType() == ValueType.INTEGER) {
            return new BigDecimal(((IntegerValue) value).getValue());
        }
        if (value.getType() == ValueType.DOUBLE) {
            return ((DoubleValue) value).getValue();
        }
        throw new IllegalArgumentException("value is not a number: " + value.getType());
    }

    public static boolean isNumber(ValueType type) {
        return type == ValueType.INTEGER || type == ValueType.DOUBLE;
    }

    public static int toIntIndex(IntegerValue value) {
        return value.getValue().intValueExact();
    }
}
