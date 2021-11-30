package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.value.BaseValue;

public class NameNode extends AbstractBaseNode {

    public final Token name;

    public NameNode(Token name) {
        this.name = name;
    }

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return VisitorRepository.getExpressionVisitor().eval(this, scope, context);
    }

}
