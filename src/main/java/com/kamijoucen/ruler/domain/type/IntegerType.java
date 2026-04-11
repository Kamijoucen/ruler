package com.kamijoucen.ruler.domain.type;

public class IntegerType implements RulerType {

    public static final IntegerType INSTANCE = new IntegerType();

    private IntegerType() {
    }

    @Override
    public TypeKind getKind() {
        return TypeKind.INT;
    }

}
