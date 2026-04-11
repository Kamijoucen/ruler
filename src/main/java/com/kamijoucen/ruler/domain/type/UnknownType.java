package com.kamijoucen.ruler.domain.type;

public class UnknownType implements RulerType {

    public static final UnknownType INSTANCE = new UnknownType();

    private UnknownType() {
    }

    @Override
    public TypeKind getKind() {
        return TypeKind.UNKNOWN;
    }

}
