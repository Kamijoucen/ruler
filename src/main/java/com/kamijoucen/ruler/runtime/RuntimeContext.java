package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.config.impl.ImportCacheManager;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;
import com.kamijoucen.ruler.value.NullValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO 如果后期支持多线程，这里的上下文与每个线程绑定，抽象一个对于一次执行公用的上下文
public class RuntimeContext {

    private final RulerConfiguration configuration;
    private Map<String, BaseValue> outSpace;
    private final Map<String, ClosureValue> infixOperationSpace;
    private NodeVisitor nodeVisitor;
    private ImportCacheManager importCache;
    private final StackDepthCheckOperation stackDepthCheckOperation;
    private BaseValue currentSelfValue;

    private boolean breakFlag = false;
    private boolean continueFlag = false;
    private boolean returnFlag = false;

    private List<BaseValue> returnSpace;

    public RuntimeContext(NodeVisitor nodeVisitor, ImportCacheManager importCache,
            StackDepthCheckOperation stackDepthCheckOperation, RulerConfiguration configuration) {
        this.outSpace = new HashMap<>();
        this.infixOperationSpace = new HashMap<>();
        this.nodeVisitor = nodeVisitor;
        this.importCache = importCache;
        this.stackDepthCheckOperation = stackDepthCheckOperation;
        this.configuration = configuration;
    }

    public boolean isBreakFlag() {
        return breakFlag;
    }

    public void setBreakFlag(boolean breakFlag) {
        this.breakFlag = breakFlag;
    }

    public boolean isContinueFlag() {
        return continueFlag;
    }

    public void setContinueFlag(boolean continueFlag) {
        this.continueFlag = continueFlag;
    }

    public boolean isReturnFlag() {
        return returnFlag;
    }

    public void setReturnFlag(boolean returnFlag) {
        this.returnFlag = returnFlag;
    }

    public BaseValue findOutValue(String name) {
        BaseValue outBaseValue = outSpace.get(name);
        if (outBaseValue == null) {
            return NullValue.INSTANCE;
        }
        return outBaseValue;
    }

    public NodeVisitor getNodeVisitor() {
        return nodeVisitor;
    }

    public ImportCacheManager getImportCache() {
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

    public void setImportCache(ImportCacheManager importCache) {
        this.importCache = importCache;
    }

    public BaseValue getCurrentSelfValue() {
        return currentSelfValue;
    }

    public void setCurrentSelfValue(BaseValue currentSelfValue) {
        this.currentSelfValue = currentSelfValue;
    }

    // hasReturnValue
    public boolean hasReturnValue() {
        return CollectionUtil.isNotEmpty(returnSpace);
    }

    public List<BaseValue> getReturnSpace() {
        return returnSpace;
    }

    public void setReturnSpace(List<BaseValue> returnSpace) {
        this.returnSpace = returnSpace;
    }

    public void addReturnSpace(BaseValue value) {
        if (returnSpace == null) {
            returnSpace = CollectionUtil.list();
        }
        returnSpace.add(value);
    }

    public void clearReturnSpace() {
        this.returnSpace = null;
    }

}
