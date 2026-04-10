package com.kamijoucen.ruler.domain.ast.factor;

import com.kamijoucen.ruler.domain.ast.AbstractBaseNode;
import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.value.BaseValue;

import java.util.List;

public class ArrayNode extends AbstractBaseNode {

    private List<BaseNode> values;

    public ArrayNode(List<BaseNode> values, TokenLocation location) {
        super(location);
        this.values = values;
    }

    @Override
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public BaseValue typeCheck(Scope scope, RuntimeContext context) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
    }


    public List<BaseNode> getValues() {
        return values;
    }

    public void setValues(List<BaseNode> values) {
        this.values = values;
    }

}
