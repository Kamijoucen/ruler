package com.kamijoucen.ruler.domain.ast.factor;

import com.kamijoucen.ruler.domain.ast.AbstractBaseNode;
import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.value.BaseValue;

import java.util.Map;

public class RsonNode extends AbstractBaseNode {

    private Map<String, BaseNode> properties;

    public RsonNode(Map<String, BaseNode> properties, TokenLocation location) {
        super(location);
        this.properties = properties;
    }

    @Override
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public BaseValue typeCheck(Scope scope, RuntimeContext context) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
    }

    public Map<String, BaseNode> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, BaseNode> properties) {
        this.properties = properties;
    }

}
