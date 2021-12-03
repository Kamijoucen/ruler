package com.kamijoucen.ruler.ast.facotr;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
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
        return context.getNodeVisitor().eval(this, scope, context);
    }

    public Map<String, BaseNode> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, BaseNode> properties) {
        this.properties = properties;
    }

}