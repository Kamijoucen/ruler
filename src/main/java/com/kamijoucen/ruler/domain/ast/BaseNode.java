package com.kamijoucen.ruler.domain.ast;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.type.RulerType;
import com.kamijoucen.ruler.domain.value.BaseValue;

import java.io.Serializable;

public interface BaseNode extends Serializable {

    BaseValue eval(Scope scope, RuntimeContext context);

    RulerType typeCheck(Scope scope, RuntimeContext context);

    TokenLocation getLocation();
}
