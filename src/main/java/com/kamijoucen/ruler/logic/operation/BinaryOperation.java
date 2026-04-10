package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;

public interface BinaryOperation {

    BaseValue invoke(BaseNode lhs, BaseNode rhs, Scope scope, RuntimeContext context, BaseValue... params);

}
