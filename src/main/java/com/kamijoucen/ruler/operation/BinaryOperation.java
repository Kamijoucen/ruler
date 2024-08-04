package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

public interface BinaryOperation {

    BaseValue invoke(BaseNode lhs, BaseNode rhs, Environment env, RuntimeContext context, NodeVisitor visitor, BaseValue... params);

}
