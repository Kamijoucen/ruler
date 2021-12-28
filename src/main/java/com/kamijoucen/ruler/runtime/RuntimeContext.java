package com.kamijoucen.ruler.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.value.BaseValue;

public class RuntimeContext {

    private Boolean isCallLinkAssign = null;
    private List<Token> outNameToken = new ArrayList<Token>();
    private Map<String, BaseValue> outSpace;
    private final NodeVisitor nodeVisitor;
    private final NodeVisitor typeCheckVisitor;

    public RuntimeContext(Map<String, BaseValue> outSpace, NodeVisitor nodeVisitor, NodeVisitor typeCheckVisitor) {
        this.outSpace = outSpace;
        this.nodeVisitor = nodeVisitor;
        this.typeCheckVisitor = typeCheckVisitor;
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

    public NodeVisitor getTypeCheckVisitor() {
        return typeCheckVisitor;
    }

    public List<Token> getOutNameToken() {
        return outNameToken;
    }

    public void setOutNameToken(List<Token> outNameToken) {
        this.outNameToken = outNameToken;
    }
}
