package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenLocation;

import java.util.List;

public class ArrayNode extends AbstractBaseNode {

    private List<BaseNode> values;

    public ArrayNode(List<BaseNode> values, TokenLocation location) {
        super(location);
        this.values = values;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor, Scope scope, RuntimeContext context) {
        return visitor.eval(this, scope, context);
    }


    public List<BaseNode> getValues() {
        return values;
    }

    public void setValues(List<BaseNode> values) {
        this.values = values;
    }

}
