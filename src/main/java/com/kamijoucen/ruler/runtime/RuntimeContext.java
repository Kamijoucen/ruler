package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.config.impl.ImportCache;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.HashMap;
import java.util.Map;

public class RuntimeContext {

    private final RulerConfiguration configuration;
    private Map<String, BaseValue> outSpace;
    private Boolean isCallLinkAssign = null;
    private NodeVisitor nodeVisitor;
    private NodeVisitor typeCheckVisitor;
    private ImportCache importCache;
    private StackDepthCheckOperation stackDepthCheckOperation;

    public RuntimeContext(NodeVisitor nodeVisitor,
                          NodeVisitor typeCheckVisitor,
                          ImportCache importCache,
                          StackDepthCheckOperation stackDepthCheckOperation,
                          RulerConfiguration configuration) {
        this.outSpace = new HashMap<String, BaseValue>();
        this.nodeVisitor = nodeVisitor;
        this.typeCheckVisitor = typeCheckVisitor;
        this.importCache = importCache;
        this.stackDepthCheckOperation = stackDepthCheckOperation;
        this.configuration = configuration;
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

    public Scope getGlobalScope() {
        return configuration.getGlobalScope();
    }

    public RulerConfiguration getConfiguration() {
        return configuration;
    }

    public StackDepthCheckOperation getStackDepthCheckOperation() {
        return stackDepthCheckOperation;
    }

    public void setStackDepthCheckOperation(StackDepthCheckOperation stackDepthCheckOperation) {
        this.stackDepthCheckOperation = stackDepthCheckOperation;
    }

    public Map<String, BaseValue> getOutSpace() {
        return outSpace;
    }

    public void setOutSpace(Map<String, BaseValue> outSpace) {
        if (outSpace == null) {
            return;
        }
        this.outSpace = outSpace;
    }

    public void setNodeVisitor(NodeVisitor nodeVisitor) {
        this.nodeVisitor = nodeVisitor;
    }

    public void setTypeCheckVisitor(NodeVisitor typeCheckVisitor) {
        this.typeCheckVisitor = typeCheckVisitor;
    }

    public void setImportCache(ImportCache importCache) {
        this.importCache = importCache;
    }
}
