package com.kamijoucen.ruler.runtime;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.config.impl.ImportCache;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.value.BaseValue;

public class RuntimeContext {

    private RulerConfiguration configuration;
    private Boolean isCallLinkAssign = null;
    private Map<String, BaseValue> outSpace;
    private final NodeVisitor nodeVisitor;
    private final NodeVisitor typeCheckVisitor;
    private final ImportCache importCache;
    private StackDepthCheckOperation stackDepthCheckOperation;

    public RuntimeContext(Map<String, BaseValue> outSpace,
                          NodeVisitor nodeVisitor,
                          NodeVisitor typeCheckVisitor,
                          ImportCache importCache, RulerConfiguration configuration,
                          StackDepthCheckOperation stackDepthCheckOperation) {
        this.outSpace = outSpace;
        this.nodeVisitor = nodeVisitor;
        this.typeCheckVisitor = typeCheckVisitor;
        this.importCache = importCache;
        this.configuration = configuration;
        this.stackDepthCheckOperation = stackDepthCheckOperation;
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

}
