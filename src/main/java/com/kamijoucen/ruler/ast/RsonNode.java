package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.Map;

public class RsonNode extends AbstractBaseNode {
    
    private Map<String, BaseNode> properties;
    
    public RsonNode(Map<String, BaseNode> properties) {
        this.properties = properties;
    }

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return VisitorRepository.getExpressionVisitor().eval(this, scope, context);
    }

    public Map<String, BaseNode> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, BaseNode> properties) {
        this.properties = properties;
    }

}
