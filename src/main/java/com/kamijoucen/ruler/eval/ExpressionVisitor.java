package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.*;
import com.kamijoucen.ruler.ast.statement.ImportNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public interface ExpressionVisitor {

    BaseValue eval(NameNode node, Scope scope, RuntimeContext context);

    BaseValue eval(OutNameNode node, Scope scope, RuntimeContext context);

    BaseValue eval(IntegerNode node, Scope scope, RuntimeContext context);

    BaseValue eval(DoubleNode node, Scope scope, RuntimeContext context);

    BaseValue eval(BoolNode node, Scope scope, RuntimeContext context);

    BaseValue eval(StringNode node, Scope scope, RuntimeContext context);

    BaseValue eval(BinaryOperationNode node, Scope scope, RuntimeContext context);

    BaseValue eval(LogicBinaryOperationNode node, Scope scope, RuntimeContext context);

    BaseValue eval(ArrayNode node, Scope scope, RuntimeContext context);

    BaseValue eval(NullNode node, Scope scope, RuntimeContext context);

    BaseValue eval(RsonNode node, Scope scope, RuntimeContext context);

    BaseValue eval(ThisNode node, Scope scope, RuntimeContext context);
}
