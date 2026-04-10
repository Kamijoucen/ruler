package com.kamijoucen.ruler.logic;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;

public interface BaseEval<T extends BaseNode> {
    BaseValue eval(T node, Scope scope, RuntimeContext context);
}