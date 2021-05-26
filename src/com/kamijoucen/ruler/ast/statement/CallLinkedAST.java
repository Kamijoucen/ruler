package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class CallLinkedAST implements BaseAST {

    private NameAST first;

    private List<BaseAST> calls;

    public CallLinkedAST(NameAST first, List<BaseAST> calls) {
        this.first = first;
        this.calls = calls;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, scope);
    }

    public NameAST getFirst() {
        return first;
    }

    public void setFirst(NameAST first) {
        this.first = first;
    }

    public List<BaseAST> getCalls() {
        return calls;
    }

    public void setCalls(List<BaseAST> calls) {
        this.calls = calls;
    }
}
