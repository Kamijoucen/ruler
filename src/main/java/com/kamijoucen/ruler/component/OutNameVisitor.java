package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.ast.factor.OutNameNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.value.BaseValue;

import java.util.ArrayList;
import java.util.List;

public class OutNameVisitor extends AbstractVisitor<BaseValue> {

    public final List<Token> outNameTokens = new ArrayList<>();

    @Override
    public BaseValue eval(OutNameNode node, Scope scope, RuntimeContext context) {
        outNameTokens.add(node.name);
        return super.eval(node, scope, context);
    }
}
