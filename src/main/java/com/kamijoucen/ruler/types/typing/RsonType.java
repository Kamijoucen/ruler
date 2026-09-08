package com.kamijoucen.ruler.types.typing;

public class RsonType implements RulerType {

    public static final RsonType INSTANCE = new RsonType();

    private RsonType() {
    }

    @Override
    public TypeKind getKind() {
        return TypeKind.RSON;
    }

}
