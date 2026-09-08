package com.kamijoucen.ruler.types.typing;

public class DoubleType implements RulerType {

    public static final DoubleType INSTANCE = new DoubleType();

    private DoubleType() {
    }

    @Override
    public TypeKind getKind() {
        return TypeKind.DOUBLE;
    }

}
