package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.exception.VariableException;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 表示作用域的类，用于管理变量的存储和查找
 * 支持嵌套作用域和作用域链查找
 */
public class Scope {

    private final boolean isCallScope;
    private final TokenLocation callLocation;
    private final String stackName;
    private final Scope parentScope;
    private final Map<String, BaseValue> valueSpace;

    /**
     * 创建一个新的作用域
     *
     * @param stackName 栈名称，用于调试和错误信息
     * @param isCallScope 是否是函数调用产生的作用域
     * @param parentScope 父作用域，可以为null（表示全局作用域）
     * @param callLocation 调用位置信息，用于错误追踪
     */
    public Scope(String stackName, boolean isCallScope, Scope parentScope, TokenLocation callLocation) {
        this.stackName = Objects.requireNonNull(stackName, "stackName cannot be null");
        this.parentScope = parentScope;
        this.valueSpace = new HashMap<>();
        this.isCallScope = isCallScope;
        this.callLocation = callLocation;
    }

    public String getStackName() {
        return stackName;
    }

    /**
     * 在作用域链中查找变量
     *
     * @param name 变量名
     * @return 变量值，如果未找到返回null
     */
    public BaseValue find(String name) {
        if (name == null) {
            return null;
        }

        BaseValue value = valueSpace.get(name);
        if (value != null) {
            return value;
        } else if (parentScope != null) {
            return parentScope.find(name);
        } else {
            return null;
        }
    }

    /**
     * 更新变量的值，会在作用域链中查找该变量
     *
     * @param name 变量名
     * @param value 新值
     * @throws VariableException 如果变量未定义
     */
    public void update(String name, BaseValue value) {
        Objects.requireNonNull(name, "Variable name cannot be null");
        Objects.requireNonNull(value, "Variable value cannot be null");

        if (valueSpace.containsKey(name)) {
            putLocal(name, value);
        } else if (parentScope != null) {
            parentScope.update(name, value);
        } else {
            throw VariableException.undefined(name, null);
        }
    }

    /**
     * 在当前作用域定义新变量
     *
     * @param name 变量名
     * @param value 变量值
     * @throws VariableException 如果变量已存在
     */
    public void defineLocal(String name, BaseValue value) {
        Objects.requireNonNull(name, "Variable name cannot be null");
        Objects.requireNonNull(value, "Variable value cannot be null");

        if (valueSpace.containsKey(name)) {
            throw VariableException.alreadyDefined(name, null);
        }
        putLocal(name, value);
    }

    /**
     * 仅在当前作用域查找变量，不查找父作用域
     *
     * @param name 变量名
     * @return 变量值，如果未找到返回null
     */
    public BaseValue getByLocal(String name) {
        return name == null ? null : valueSpace.get(name);
    }

    /**
     * 在当前作用域设置变量值
     *
     * @param name 变量名
     * @param value 变量值
     */
    public void putLocal(String name, BaseValue value) {
        Objects.requireNonNull(name, "Variable name cannot be null");
        Objects.requireNonNull(value, "Variable value cannot be null");
        valueSpace.put(name, value);
    }

    /**
     * 从当前作用域移除变量
     *
     * @param name 变量名
     */
    public void remove(String name) {
        if (name != null) {
            valueSpace.remove(name);
        }
    }

    public boolean isCallScope() {
        return isCallScope;
    }

    public TokenLocation getCallLocation() {
        return callLocation;
    }

    /**
     * 获取父作用域
     *
     * @return 父作用域，可能为null
     */
    public Scope getParentScope() {
        return parentScope;
    }
}
