package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.eval.BaseEval;

public abstract class AbstractBaseNode implements BaseNode {

    protected BaseEval eval;

    @Override
    public void bindEval() {
    }
}
