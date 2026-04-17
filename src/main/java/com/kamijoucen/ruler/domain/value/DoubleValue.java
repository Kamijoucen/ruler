package com.kamijoucen.ruler.domain.value;

import java.math.BigDecimal;
import java.util.Objects;

public class DoubleValue extends AbstractValue {

    private final BigDecimal value;

    public DoubleValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.DOUBLE;
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DoubleValue) {
            return value.compareTo(((DoubleValue) obj).value) == 0;
        }
        if (obj instanceof Number) {
            return value.doubleValue() == ((Number) obj).doubleValue();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
