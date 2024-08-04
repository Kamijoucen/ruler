package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.common.RStack;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.Iterator;

public class Environment {

    private final Scope globalScope;

    private final RStack<Scope> scopeStack;

    public Environment(Scope globalScope) {
        this.globalScope = globalScope;
        this.scopeStack = new RStack<>();
        this.scopeStack.push(this.globalScope);
    }

    public void pop() {
        scopeStack.pop();
    }

    public void push(String stackName) {
        scopeStack.push(new Scope(stackName, null));
    }

    public void defineLocal(String name, BaseValue value) {
        Scope currentScope = currentScope();
        if (currentScope.find(name) != null) {
            // TODO 静态检查，无需运行时检查
            throw SyntaxException.withSyntax("变量已定义：" + name);
        }
        currentScope.define(name, value);
    }

    public BaseValue find(String name) {
        BaseValue value = null;
        Iterator<Scope> iterator = scopeStack.iterator();
        while (iterator.hasNext()) {
            Scope scope = iterator.next();
            value = scope.find(name);
            if (value != null) {
                break;
            }
        }
        return value;
    }

    // update
    public void update(String name, BaseValue value) {
        Iterator<Scope> iterator = scopeStack.iterator();
        while (iterator.hasNext()) {
            Scope scope = iterator.next();
            if (scope.find(name) != null) {
                scope.update(name, value);
                return;
            }
        }
    }

    private Scope currentScope() {
        return scopeStack.peek();
    }
}