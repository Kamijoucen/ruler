package com.kamijoucen.ruler.parse.impl;

import java.util.Stack;

public class ParseContext {

    private Stack<LexicalScope> lexicalScope;

    public ParseContext() {
        lexicalScope = new Stack<LexicalScope>();
    }

    public void pushScope(LexicalScope item) {
        lexicalScope.push(item);
    }

    public LexicalScope popScope() {
        return lexicalScope.pop();
    }


    public LexicalScope getCurrentScope() {
        return lexicalScope.peek();
    }
}
