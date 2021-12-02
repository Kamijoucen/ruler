package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.eval.NodeVisitor;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.Map;

public class RuntimeContext {

    private Boolean isCallLinkAssign = null;

    private Map<String, BaseValue> outSpace;

    private final NodeVisitor nodeVisitor;

    public RuntimeContext(Map<String, BaseValue> outSpace, NodeVisitor nodeVisitor) {
        this.outSpace = outSpace;
        this.nodeVisitor = nodeVisitor;
    }

    public BaseValue findOutValue(String name) {
        return outSpace.get(name);
    }

    public Boolean getCallLinkAssign() {
        return isCallLinkAssign;
    }

    public void setCallLinkAssign(Boolean callLinkAssign) {
        isCallLinkAssign = callLinkAssign;
    }

    public NodeVisitor getNodeVisitor() {
        return nodeVisitor;
    }
}
