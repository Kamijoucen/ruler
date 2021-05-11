package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.runtime.VisitorRepository;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.value.BaseValue;

public class NameAST implements BaseAST {

    public final Token name;

    public final boolean isOut;

    public NameAST(Token name, boolean isOut) {
        this.name = name;
        this.isOut = isOut;
    }

    @Override

    public BaseValue eval(Scope scope) {
        return VisitorRepository.getExpressionVisitor().eval(this, scope);
    }

}
