package com.kamijoucen.ruler.domain.type;

public interface RulerType {

    TypeKind getKind();

    default boolean isNumeric() {
        return getKind() == TypeKind.INT || getKind() == TypeKind.DOUBLE;
    }

    default boolean isKnown() {
        return getKind() != TypeKind.UNKNOWN && getKind() != TypeKind.FAILURE;
    }

}
