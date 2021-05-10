package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;

public class BlockAST implements BaseAST {

    private List<BaseAST> blocks;

    public BlockAST(List<BaseAST> blocks) {
        this.blocks = blocks;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return null;
    }

    public List<BaseAST> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BaseAST> blocks) {
        this.blocks = blocks;
    }
}
