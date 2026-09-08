package com.kamijoucen.ruler.types.typing;

public class BoolType implements RulerType {

    public static final BoolType INSTANCE = new BoolType();

    private BoolType() {
    }

    @Override
    public TypeKind getKind() {
        return TypeKind.BOOL;
    }

}
