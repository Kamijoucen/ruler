package com.kamijoucen.ruler.types.typing;

public class StringType implements RulerType {

    public static final StringType INSTANCE = new StringType();

    private StringType() {
    }

    @Override
    public TypeKind getKind() {
        return TypeKind.STRING;
    }

}
