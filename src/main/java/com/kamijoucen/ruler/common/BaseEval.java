package com.kamijoucen.ruler.common;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;

public interface BaseEval<T extends BaseNode> {

    EvalResult eval(T node, Scope scope, RuntimeContext context);

}