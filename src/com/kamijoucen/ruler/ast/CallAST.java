package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.common.VisitorRepository;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class CallAST implements BaseAST {

    private NameAST name;

    private List<BaseAST> param;

    public CallAST(NameAST name, List<BaseAST> param) {
        this.name = name;
        this.param = param;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return VisitorRepository.getStatementVisitor().eval(this, scope);
    }

    public NameAST getName() {
        return name;
    }

    public void setName(NameAST name) {
        this.name = name;
    }

    public List<BaseAST> getParam() {
        return param;
    }

    public void setParam(List<BaseAST> param) {
        this.param = param;
    }
}
