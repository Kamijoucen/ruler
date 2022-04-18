package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

public interface BaseNode {

    BaseValue eval(RuntimeContext context, Scope scope);

    BaseValue typeCheck(RuntimeContext context, Scope scope);

    TokenLocation getLocation();
}
