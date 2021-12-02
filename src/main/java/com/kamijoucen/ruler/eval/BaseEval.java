package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public interface BaseEval<T extends BaseNode> {
    BaseValue eval(T node, Scope scope, RuntimeContext context);
}