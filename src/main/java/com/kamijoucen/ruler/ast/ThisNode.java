package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class ThisNode implements BaseNode {
    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getExpressionVisitor().eval(this, scope);
    }
}
