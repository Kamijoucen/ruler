package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.eval.NodeVisitor;
import com.kamijoucen.ruler.parse.Lexical;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RuntimeContext {

    private Boolean isCallLinkAssign = null;
    private List<Token> outNameToken = new ArrayList<Token>();
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

    public List<Token> getOutNameToken() {
        return outNameToken;
    }

    public void setOutNameToken(List<Token> outNameToken) {
        this.outNameToken = outNameToken;
    }
}
