package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.*;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public interface ExpressionVisitor {

    BaseValue eval(NameNode ast, RuntimeContext context);

    BaseValue eval(IntegerNode ast, RuntimeContext context);

    BaseValue eval(DoubleNode ast, RuntimeContext context);

    BaseValue eval(BoolNode ast, RuntimeContext context);

    BaseValue eval(StringNode ast, RuntimeContext context);

    BaseValue eval(BinaryOperationNode ast, RuntimeContext context);

    BaseValue eval(LogicBinaryOperationNode node, RuntimeContext context);

    BaseValue eval(ArrayNode node, RuntimeContext context);

    BaseValue eval(NullNode node, RuntimeContext context);

}
