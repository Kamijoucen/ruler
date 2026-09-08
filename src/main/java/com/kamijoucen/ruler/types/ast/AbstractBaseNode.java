package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.token.TokenLocation;

public abstract class AbstractBaseNode implements BaseNode {

    private final TokenLocation location;

    public AbstractBaseNode(TokenLocation location) {
        this.location = location;
    }

    @Override
    public TokenLocation getLocation() {
        return location;
    }

}
