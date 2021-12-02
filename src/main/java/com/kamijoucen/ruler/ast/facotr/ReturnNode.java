package com.kamijoucen.ruler.ast.facotr;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class ReturnNode extends AbstractBaseNode {

    private List<BaseNode> param;

    public ReturnNode(List<BaseNode> param) {
        this.param = param;
    }

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    public List<BaseNode> getParam() {
        return param;
    }

    public void setParam(List<BaseNode> param) {
        this.param = param;
    }
}
