package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.config.impl.ImportCache;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;

import java.util.Map;

public interface RuntimeContext2 {

    BaseValue findOutValue(String name);

    Boolean getCallLinkAssign();

    void setCallLinkAssign(Boolean callLinkAssign);

    NodeVisitor getNodeVisitor();

    NodeVisitor getTypeCheckVisitor();

    ImportCache getImportCache();

    Scope getGlobalScope();

    RulerConfiguration getConfiguration();

    StackDepthCheckOperation getStackDepthCheckOperation();

    Map<String, ClosureValue> getInfixOperationSpace();

    void addInfixOperation(String name, ClosureValue infixOperationSpace);

    ClosureValue getInfixOperation(String name);

    void setNodeVisitor(NodeVisitor nodeVisitor);

    void setTypeCheckVisitor(NodeVisitor typeCheckVisitor);

    void setImportCache(ImportCache importCache);

}
