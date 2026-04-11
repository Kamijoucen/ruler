package com.kamijoucen.ruler.domain.type;

public class ArrayType implements RulerType {

    public static final ArrayType INSTANCE = new ArrayType();

    private ArrayType() {
    }

    @Override
    public TypeKind getKind() {
        return TypeKind.ARRAY;
    }

}
