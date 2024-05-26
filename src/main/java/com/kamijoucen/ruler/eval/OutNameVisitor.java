package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.factor.OutNameNode;
import com.kamijoucen.ruler.common.AbstractVisitor;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.ArrayList;
import java.util.List;

public class OutNameVisitor extends AbstractVisitor {

    public final List<Token> outNameTokens = new ArrayList<>();

    @Override
    public BaseValue eval(OutNameNode node, Scope scope, RuntimeContext context) {
        outNameTokens.add(node.name);
        return super.eval(node, scope, context);
    }
}
