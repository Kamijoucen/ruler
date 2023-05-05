package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

import java.io.Serializable;

public interface BaseNode extends Serializable {

    BaseValue eval(Scope scope, RuntimeContext context);

    BaseValue typeCheck(Scope scope, RuntimeContext context);

    TokenLocation getLocation();
}
