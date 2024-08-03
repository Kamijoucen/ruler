package com.kamijoucen.ruler.compiler.symbol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.kamijoucen.ruler.common.RStack;

public class SymbolTable {

    /**
     * TODO 符号表一维数组即可，parse过程中，为所有符号分配一个自增id作为索引
     */
    private final RStack<Map<String, Symbol>> symbolStack = new RStack<>();

    public void push() {
        symbolStack.push(new HashMap<>());
    }

    public void pop() {
        symbolStack.pop();
    }

    public Symbol findCurrentScope(String name) {
        return symbolStack.peek().get(name);
    }

    public Symbol find(String name) {
        Iterator<Map<String, Symbol>> iterator = symbolStack.iterator();
        Symbol result = null;
        while (iterator.hasNext()) {
            Map<String, Symbol> map = iterator.next();
            result = map.get(name);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    public void define(Symbol symbol) {
        symbolStack.peek().put(symbol.token.name, symbol);
    }

}
