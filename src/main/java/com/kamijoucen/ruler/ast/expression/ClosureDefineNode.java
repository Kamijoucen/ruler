package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

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
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
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
