package com.kamijoucen.ruler.ast.factor;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class ReturnNode extends AbstractBaseNode {

    private List<BaseNode> param;

    public ReturnNode(List<BaseNode> param, TokenLocation location) {
        super(location);
        this.param = param;
    }

    @Override
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
    }

    public List<BaseNode> getParam() {
        return param;
    }

    public void setParam(List<BaseNode> param) {
        this.param = param;
    }
}
