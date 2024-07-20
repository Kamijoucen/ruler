package com.kamijoucen.ruler.ast.factor;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.Map;

public class RsonNode extends AbstractBaseNode {

    private Map<String, BaseNode> properties;

    public RsonNode(Map<String, BaseNode> properties, TokenLocation location) {
        super(location);
        this.properties = properties;
    }

    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }
    public Map<String, BaseNode> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, BaseNode> properties) {
        this.properties = properties;
    }

}
