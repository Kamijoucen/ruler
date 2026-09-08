package com.kamijoucen.ruler.types.typing;

public class UnknownType implements RulerType {

    public static final UnknownType INSTANCE = new UnknownType();

    private UnknownType() {
    }

    @Override
    public TypeKind getKind() {
        return TypeKind.UNKNOWN;
    }

}
