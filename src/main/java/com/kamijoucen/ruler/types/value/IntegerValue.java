package com.kamijoucen.ruler.types.value;

import java.math.BigInteger;
import java.util.Objects;

public class IntegerValue extends AbstractValue {

    private static final int CACHE_SIZE = 1024;
    private static final BigInteger CACHE_LIMIT = BigInteger.valueOf(CACHE_SIZE);
    private static final IntegerValue[] CACHE = createCache();
    private final BigInteger value;

    private static IntegerValue[] createCache() {
        IntegerValue[] values = new IntegerValue[CACHE_SIZE];
        for (int i = 0; i < values.length; i++) {
            values[i] = new IntegerValue(BigInteger.valueOf(i));
        }
        return values;
    }

    public static IntegerValue valueOf(BigInteger value) {
        Objects.requireNonNull(value, "value");
        if (value.signum() >= 0 && value.compareTo(CACHE_LIMIT) < 0) {
            return CACHE[value.intValue()];
        }
        return new IntegerValue(value);
    }

    public IntegerValue(BigInteger value) {
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.INTEGER;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public BigInteger getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof IntegerValue) {
            return value.equals(((IntegerValue) obj).value);
        }
        if (obj instanceof Number) {
            return value.longValue() == ((Number) obj).longValue();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
