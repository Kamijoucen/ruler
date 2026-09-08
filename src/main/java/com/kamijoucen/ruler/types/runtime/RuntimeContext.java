package com.kamijoucen.ruler.types.runtime;

import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.logic.util.CollectionUtil;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.ClosureValue;
import com.kamijoucen.ruler.types.value.NullValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/** Mutable state belonging to one execution. Imported modules share only the import chain. */
public class RuntimeContext {

    private final RulerConfiguration configuration;
    private Map<String, BaseValue> outSpace;
    private final Map<String, ClosureValue> infixOperationSpace;
    private final List<String> importStack;
    private final CallDepth callDepth;

    private boolean breakFlag = false;
    private boolean continueFlag = false;
    private boolean returnFlag = false;

    private List<BaseValue> returnSpace;
    private TypeScope typeScope;

    public RuntimeContext(RulerConfiguration configuration) {
        this(configuration, new ArrayList<>());
    }

    private RuntimeContext(RulerConfiguration configuration, List<String> importStack) {
        this.configuration = configuration;
        this.importStack = importStack;
        this.outSpace = new HashMap<>();
        this.infixOperationSpace = new HashMap<>();
        this.callDepth = new CallDepth();
        this.typeScope = new TypeScope(null);
    }

    public RuntimeContext createImportContext() {
        return new RuntimeContext(configuration, importStack);
    }

    public List<String> getImportStack() {
        return importStack;
    }

    public boolean isBreakFlag() {
        return breakFlag;
    }

    public void setBreakFlag(boolean breakFlag) {
        this.breakFlag = breakFlag;
    }

    public boolean consumeBreakFlag() {
        if (!breakFlag) {
            return false;
        }
        breakFlag = false;
        return true;
    }

    public boolean isContinueFlag() {
        return continueFlag;
    }

    public void setContinueFlag(boolean continueFlag) {
        this.continueFlag = continueFlag;
    }

    public boolean consumeContinueFlag() {
        if (!continueFlag) {
            return false;
        }
        continueFlag = false;
        return true;
    }

    public boolean isReturnFlag() {
        return returnFlag;
    }

    public void setReturnFlag(boolean returnFlag) {
        this.returnFlag = returnFlag;
    }

    public void clearControlFlags() {
        breakFlag = false;
        continueFlag = false;
        clearReturnState();
    }

    public void clearLoopFlags() {
        breakFlag = false;
        continueFlag = false;
    }

    public void clearReturnState() {
        returnFlag = false;
        returnSpace = null;
    }

    public void startReturn(List<BaseValue> values) {
        returnFlag = true;
        returnSpace = values;
    }

    public <T> T withIsolatedReturn(Supplier<T> supplier) {
        boolean outerReturnFlag = returnFlag;
        List<BaseValue> outerReturnSpace = returnSpace;
        clearReturnState();
        try {
            return supplier.get();
        } finally {
            returnFlag = outerReturnFlag;
            returnSpace = outerReturnSpace;
        }
    }

    public BaseValue findOutValue(String name) {
        BaseValue outBaseValue = outSpace.get(name);
        if (outBaseValue == null) {
            return NullValue.INSTANCE;
        }
        return outBaseValue;
    }

    public Scope getGlobalScope() {
        return configuration.getGlobalScope();
    }

    public RulerConfiguration getConfiguration() {
        return configuration;
    }

    public CallDepth getCallDepth() {
        return callDepth;
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

    public TypeScope getTypeScope() {
        return typeScope;
    }

    public void setTypeScope(TypeScope typeScope) {
        this.typeScope = typeScope;
    }

}
