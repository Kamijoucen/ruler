package com.kamijoucen.ruler.runtime;

import java.util.Map;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.value.BaseValue;

public class RuntimeContext {

    private Boolean isCallLinkAssign = null;
    private Map<String, BaseValue> outSpace;
    private final NodeVisitor nodeVisitor;
    private final NodeVisitor typeCheckVisitor;
    private final ImportCache importCache;

    public RuntimeContext(Map<String, BaseValue> outSpace, NodeVisitor nodeVisitor, NodeVisitor typeCheckVisitor, ImportCache importCache) {
        this.outSpace = outSpace;
        this.nodeVisitor = nodeVisitor;
        this.typeCheckVisitor = typeCheckVisitor;
        this.importCache = importCache;
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

    public ImportCache getImportCache() {
        return importCache;
    }
}
