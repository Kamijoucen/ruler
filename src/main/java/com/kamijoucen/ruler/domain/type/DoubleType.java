package com.kamijoucen.ruler.domain.type;

public class DoubleType implements RulerType {

    public static final DoubleType INSTANCE = new DoubleType();

    private DoubleType() {
    }

    @Override
    public TypeKind getKind() {
        return TypeKind.DOUBLE;
    }

}
