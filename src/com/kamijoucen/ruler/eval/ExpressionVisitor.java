package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.*;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public interface ExpressionVisitor {

    BaseValue eval(NameNode ast, Scope scope);

    BaseValue eval(IntegerNode ast, Scope scope);

    BaseValue eval(DoubleNode ast, Scope scope);

    BaseValue eval(BoolNode ast, Scope scope);

    BaseValue eval(StringNode ast, Scope scope);

    BaseValue eval(BinaryOperationNode ast, Scope scope);

    BaseValue eval(ArrayNode node, Scope scope);

    BaseValue eval(NullNode node, Scope scope);

}
