package com.kamijoucen.ruler.domain.type;

public class NullType implements RulerType {

    public static final NullType INSTANCE = new NullType();

    private NullType() {
    }

    @Override
    public TypeKind getKind() {
        return TypeKind.NULL;
    }

}
