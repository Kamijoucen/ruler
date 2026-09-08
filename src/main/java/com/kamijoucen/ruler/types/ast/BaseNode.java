package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenLocation;

import java.io.Serializable;

public interface BaseNode extends Serializable {

    <T> T accept(NodeVisitor<T> visitor, Scope scope, RuntimeContext context);

    TokenLocation getLocation();
}
