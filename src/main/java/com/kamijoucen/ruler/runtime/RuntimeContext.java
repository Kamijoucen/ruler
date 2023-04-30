package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.config.impl.ImportCache;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;
import com.kamijoucen.ruler.value.constant.NullValue;

import java.util.HashMap;
import java.util.Map;

public class RuntimeContext {

    private final RulerConfiguration configuration;
    private Map<String, BaseValue> outSpace;
    private Map<String, ClosureValue> infixOperationSpace;
    private Boolean isCallChainAssign = null;
    private NodeVisitor nodeVisitor;
    private NodeVisitor typeCheckVisitor;
    private ImportCache importCache;
    private StackDepthCheckOperation stackDepthCheckOperation;

    public RuntimeContext(NodeVisitor nodeVisitor,
                          NodeVisitor typeCheckVisitor,
                          ImportCache importCache,
                          StackDepthCheckOperation stackDepthCheckOperation,
                          RulerConfiguration configuration) {
        this.outSpace = new HashMap<>();
        this.infixOperationSpace = new HashMap<>();
        this.nodeVisitor = nodeVisitor;
        this.typeCheckVisitor = typeCheckVisitor;
        this.importCache = importCache;
        this.stackDepthCheckOperation = stackDepthCheckOperation;
        this.configuration = configuration;
    }

    public BaseValue findOutValue(String name) {
        BaseValue outBaseValue = outSpace.get(name);
        if (outBaseValue == null) {
            return NullValue.INSTANCE;
        }
        return outBaseValue;
    }

    public Boolean getCallChainAssign() {
        return isCallChainAssign;
    }

    public void setCallChainAssign(Boolean callChainAssign) {
        isCallChainAssign = callChainAssign;
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

    public void setOutSpace(Map<String, BaseValue> outSpace) {
        if (outSpace == null) {
            return;
        }
        this.outSpace = outSpace;
    }

    public Map<String, ClosureValue> getInfixOperationSpace() {
        return infixOperationSpace;
    }

    public void addInfixOperation(String name, ClosureValue infixOperationSpace) {
        this.infixOperationSpace.put(name, infixOperationSpace);
    }

    public ClosureValue getInfixOperation(String name) {
        return infixOperationSpace.get(name);
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
