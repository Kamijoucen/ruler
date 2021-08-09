package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.*;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public interface ExpressionVisitor {

    BaseValue eval(NameNode node, Scope scope);

    BaseValue eval(OutNameNode node, Scope scope);

    BaseValue eval(IntegerNode node, Scope scope);

    BaseValue eval(DoubleNode node, Scope scope);

    BaseValue eval(BoolNode node, Scope scope);

    BaseValue eval(StringNode node, Scope scope);

    BaseValue eval(BinaryOperationNode node, Scope scope);

    BaseValue eval(LogicBinaryOperationNode node, Scope scope);

    BaseValue eval(ArrayNode node, Scope scope);

    BaseValue eval(NullNode node, Scope scope);

    BaseValue eval(RsonNode node, Scope scope);

    BaseValue eval(ThisNode node, Scope scope);

}
