package com.kamijoucen.ruler.domain.ast;

import com.kamijoucen.ruler.domain.token.TokenLocation;

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
