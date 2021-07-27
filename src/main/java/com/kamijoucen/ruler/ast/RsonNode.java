package com.kamijoucen.ruler.ast;

import java.util.Map;

import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class RsonNode implements BaseNode {
    
    private Map<String, BaseNode> properties;
    
    public RsonNode(Map<String, BaseNode> properties) {
        this.properties = properties;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getExpressionVisitor().eval(this, scope);
    }

    public Map<String, BaseNode> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, BaseNode> properties) {
        this.properties = properties;
    }

}
