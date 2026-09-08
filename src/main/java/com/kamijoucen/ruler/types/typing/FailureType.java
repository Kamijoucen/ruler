package com.kamijoucen.ruler.types.typing;

public class FailureType implements RulerType {

    public static final FailureType INSTANCE = new FailureType();

    private FailureType() {
    }

    @Override
    public TypeKind getKind() {
        return TypeKind.FAILURE;
    }

}
