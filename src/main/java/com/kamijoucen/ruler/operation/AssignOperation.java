package com.kamijoucen.ruler.operation;


import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.OperationNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public interface AssignOperation {

    BaseValue assign(BaseValue preOperationValue, OperationNode call, BaseNode expression, Scope scope, RuntimeContext context);

}
