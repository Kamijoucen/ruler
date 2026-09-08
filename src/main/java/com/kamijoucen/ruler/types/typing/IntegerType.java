package com.kamijoucen.ruler.types.typing;

public class IntegerType implements RulerType {

    public static final IntegerType INSTANCE = new IntegerType();

    private IntegerType() {
    }

    @Override
    public TypeKind getKind() {
        return TypeKind.INT;
    }

}
