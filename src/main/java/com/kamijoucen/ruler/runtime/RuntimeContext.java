package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.config.impl.ImportCacheManager;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;
import com.kamijoucen.ruler.value.NullValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 运行时上下文，保存一次脚本执行的运行时信息
 * 注意：当前实现不是线程安全的，每个线程应该有自己的RuntimeContext实例
 *
 * @author Kamijoucen
 */
// TODO 如果后期支持多线程，这里的上下文与每个线程绑定，抽象一个对于一次执行公用的上下文
public class RuntimeContext {

    private final RulerConfiguration configuration;
    private Map<String, BaseValue> outSpace;
    private final Map<String, ClosureValue> infixOperationSpace;
    private NodeVisitor nodeVisitor;
    private NodeVisitor typeCheckVisitor;
    private ImportCacheManager importCache;
    private final StackDepthCheckOperation stackDepthCheckOperation;
    private BaseValue currentSelfValue;

    // 控制流标志
    private volatile boolean breakFlag = false;
    private volatile boolean continueFlag = false;
    private volatile boolean returnFlag = false;

    private List<BaseValue> returnSpace;

    /**
     * 创建运行时上下文
     *
     * @param nodeVisitor AST节点访问器
     * @param typeCheckVisitor 类型检查访问器
     * @param importCache 导入缓存管理器
     * @param stackDepthCheckOperation 栈深度检查操作
     * @param configuration 配置对象
     */
    public RuntimeContext(NodeVisitor nodeVisitor,
                          NodeVisitor typeCheckVisitor,
                          ImportCacheManager importCache,
                          StackDepthCheckOperation stackDepthCheckOperation,
                          RulerConfiguration configuration) {
        this.nodeVisitor = Objects.requireNonNull(nodeVisitor, "nodeVisitor cannot be null");
        this.typeCheckVisitor = Objects.requireNonNull(typeCheckVisitor, "typeCheckVisitor cannot be null");
        this.importCache = Objects.requireNonNull(importCache, "importCache cannot be null");
        this.stackDepthCheckOperation = Objects.requireNonNull(stackDepthCheckOperation, "stackDepthCheckOperation cannot be null");
        this.configuration = Objects.requireNonNull(configuration, "configuration cannot be null");
        this.outSpace = new HashMap<>();
        this.infixOperationSpace = new HashMap<>();
    }

    // 控制流标志的getter和setter方法
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

    /**
     * 查找外部传入的变量值
     *
     * @param name 变量名
     * @return 变量值，如果不存在返回NullValue.INSTANCE
     */
    public BaseValue findOutValue(String name) {
        if (name == null) {
            return NullValue.INSTANCE;
        }
        BaseValue outBaseValue = outSpace.get(name);
        return outBaseValue != null ? outBaseValue : NullValue.INSTANCE;
    }

    public NodeVisitor getNodeVisitor() {
        return nodeVisitor;
    }

    public NodeVisitor getTypeCheckVisitor() {
        return typeCheckVisitor;
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

    /**
     * 设置外部变量空间
     *
     * @param outSpace 外部变量映射表
     */
    public void setOutSpace(Map<String, BaseValue> outSpace) {
        if (outSpace == null) {
            this.outSpace = new HashMap<>();
        } else {
            // 创建副本，避免外部修改影响内部状态
            this.outSpace = new HashMap<>(outSpace);
        }
    }

    /**
     * 获取中缀操作符空间（只读）
     *
     * @return 中缀操作符映射表的不可修改视图
     */
    public Map<String, ClosureValue> getInfixOperationSpace() {
        return Collections.unmodifiableMap(infixOperationSpace);
    }

    /**
     * 添加中缀操作符
     *
     * @param name 操作符名称
     * @param infixOperation 操作符对应的闭包
     */
    public void addInfixOperation(String name, ClosureValue infixOperation) {
        Objects.requireNonNull(name, "Infix operation name cannot be null");
        Objects.requireNonNull(infixOperation, "Infix operation value cannot be null");
        this.infixOperationSpace.put(name, infixOperation);
    }

    /**
     * 获取指定的中缀操作符
     *
     * @param name 操作符名称
     * @return 操作符对应的闭包，如果不存在返回null
     */
    public ClosureValue getInfixOperation(String name) {
        return name == null ? null : infixOperationSpace.get(name);
    }

    public void setNodeVisitor(NodeVisitor nodeVisitor) {
        this.nodeVisitor = Objects.requireNonNull(nodeVisitor, "nodeVisitor cannot be null");
    }

    public void setTypeCheckVisitor(NodeVisitor typeCheckVisitor) {
        this.typeCheckVisitor = Objects.requireNonNull(typeCheckVisitor, "typeCheckVisitor cannot be null");
    }

    public void setImportCache(ImportCacheManager importCache) {
        this.importCache = Objects.requireNonNull(importCache, "importCache cannot be null");
    }

    public BaseValue getCurrentSelfValue() {
        return currentSelfValue;
    }

    public void setCurrentSelfValue(BaseValue currentSelfValue) {
        this.currentSelfValue = currentSelfValue;
    }

    /**
     * 检查是否有返回值
     *
     * @return 如果有返回值返回true
     */
    public boolean hasReturnValue() {
        return CollectionUtil.isNotEmpty(returnSpace);
    }

    /**
     * 获取返回值列表（只读）
     *
     * @return 返回值列表的不可修改视图，如果没有返回值返回null
     */
    public List<BaseValue> getReturnSpace() {
        return returnSpace == null ? null : Collections.unmodifiableList(returnSpace);
    }

    /**
     * 设置返回值列表
     *
     * @param returnSpace 返回值列表
     */
    public void setReturnSpace(List<BaseValue> returnSpace) {
        if (returnSpace == null) {
            this.returnSpace = null;
        } else {
            // 创建副本，避免外部修改
            this.returnSpace = new ArrayList<>(returnSpace);
        }
    }

    /**
     * 添加返回值
     *
     * @param value 要添加的返回值
     */
    public void addReturnSpace(BaseValue value) {
        Objects.requireNonNull(value, "Return value cannot be null");
        if (returnSpace == null) {
            returnSpace = new ArrayList<>();
        }
        returnSpace.add(value);
    }

    /**
     * 清空返回值列表
     */
    public void clearReturnSpace() {
        this.returnSpace = null;
    }

    /**
     * 重置所有控制流标志
     */
    public void resetControlFlags() {
        this.breakFlag = false;
        this.continueFlag = false;
        this.returnFlag = false;
    }
}
