package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenLocation;

import java.util.List;

public class ClosureDefineNode extends AbstractBaseNode {

    private String name;
    private List<BaseNode> param;
    private BaseNode block;
    private boolean isStaticCapture;
    private List<BaseNode> staticCaptureVar;

    public ClosureDefineNode(String name, List<BaseNode> param, BaseNode block,
            boolean isStaticCapture, List<BaseNode> staticCaptureVar, TokenLocation location) {
        super(location);
        this.name = name;
        this.param = param;
        this.block = block;
        this.isStaticCapture = isStaticCapture;
        this.staticCaptureVar = staticCaptureVar;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor, Scope scope, RuntimeContext context) {
        return visitor.eval(this, scope, context);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BaseNode> getParam() {
        return param;
    }

    public void setParam(List<BaseNode> param) {
        this.param = param;
    }

    public BaseNode getBlock() {
        return block;
    }

    public void setBlock(BaseNode block) {
        this.block = block;
    }

    public boolean isStaticCapture() {
        return isStaticCapture;
    }

    public void setStaticCapture(boolean isStaticCapture) {
        this.isStaticCapture = isStaticCapture;
    }

    public List<BaseNode> getStaticCaptureVar() {
        return staticCaptureVar;
    }

    public void setStaticCaptureVar(List<BaseNode> staticCaptureVar) {
        this.staticCaptureVar = staticCaptureVar;
    }

}
