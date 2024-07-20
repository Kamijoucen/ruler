package com.kamijoucen.ruler.ast.factor;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class ArrayNode extends AbstractBaseNode {

    private List<BaseNode> values;

    public ArrayNode(List<BaseNode> values, TokenLocation location) {
        super(location);
        this.values = values;
    }

    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }

    public List<BaseNode> getValues() {
        return values;
    }

    public void setValues(List<BaseNode> values) {
        this.values = values;
    }

}
