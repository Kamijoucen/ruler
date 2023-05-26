package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.facotr.NameNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

public class DefaultParamValueNode extends AbstractBaseNode {

    private final NameNode name;
    private final BaseNode exp;

    public DefaultParamValueNode(NameNode name, BaseNode exp, TokenLocation location) {
        super(location);
        this.name = name;
        this.exp = exp;
    }

    @Override
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public BaseValue typeCheck(Scope scope, RuntimeContext context) {
        return null;
    }

    public NameNode getName() {
        return name;
    }


    public BaseNode getExp() {
        return exp;
    }

}
