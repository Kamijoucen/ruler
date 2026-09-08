package com.kamijoucen.ruler.logic;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;

public interface BaseEval<T extends BaseNode> {
    BaseValue eval(T node, Scope scope, RuntimeContext context);
}
