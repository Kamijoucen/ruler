package com.kamijoucen.ruler.domain.value;

import java.math.BigInteger;
import java.util.Objects;

public class IntegerValue extends AbstractValue {

    private final BigInteger value;

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
