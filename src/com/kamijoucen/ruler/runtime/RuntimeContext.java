package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.common.RStack;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.constant.NullValue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RuntimeContext {

    private RStack<ContextScope> stack;

    private Map<String, BaseValue> outScope;

    public RuntimeContext() {
        this.stack = new RStack<ContextScope>();
        this.outScope = new ConcurrentHashMap<String, BaseValue>();
    }

    public void updateValue(String name, BaseValue value) {
        for (ContextScope scope : stack) {
            if (scope.containsName(name)) {
                scope.put(name, value);
                return;
            }
        }
        throw SyntaxException.withSyntax("变量 \"" + name + "\" 未定义");
    }

    public void putLocalValue(String name, BaseValue value) {
        getCurrentScope().put(name, value);
    }

    public BaseValue findValue(String name) {
        for (ContextScope scope : stack) {
            BaseValue value = scope.find(name);
            if (value != null) {
                return value;
            }
        }
        return NullValue.INSTANCE;
    }

    public BaseValue findLocalValue(String name) {
        BaseValue value = getCurrentScope().find(name);
        if (value == null) {
            return NullValue.INSTANCE;
        }
        return value;
    }

    public ContextScope getCurrentScope() {
        return this.stack.peek();
    }

    public void initReturnSpace() {
        getCurrentScope().initReturnSpace();
    }

    public void putReturnValues(List<BaseValue> values) {
        for (ContextScope scope : stack) {
            if (scope.hasReturnSpace()) {
                scope.putReturnSpace(values);
                return;
            }
        }
        throw SyntaxException.withSyntax("没有找到支持返回值的环境");
    }

    public void push(String stackName) {
        stack.push(new ContextScope(stackName));
    }

    public ContextScope pop() {
        return stack.pop();
    }

    public RStack<ContextScope> copyStackRef() {
        return stack.copy();
    }
}
