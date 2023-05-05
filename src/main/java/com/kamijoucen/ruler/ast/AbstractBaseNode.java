package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.token.TokenLocation;

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
