package com.kamijoucen.ruler.domain.ast.factor;

import com.kamijoucen.ruler.domain.ast.AbstractBaseNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.value.BaseValue;

public class OutNameNode extends AbstractBaseNode {

    public final Token name;

    public OutNameNode(Token name, TokenLocation location) {
        super(location);
        this.name = name;
    }

    @Override
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public BaseValue typeCheck(Scope scope, RuntimeContext context) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
    }


}
