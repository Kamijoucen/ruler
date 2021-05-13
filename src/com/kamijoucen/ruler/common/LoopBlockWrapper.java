package com.kamijoucen.ruler.common;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.ast.statement.BlockAST;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class LoopBlockWrapper implements BaseAST {

    private BlockAST blockAST;

    public LoopBlockWrapper(BlockAST blockAST) {
        this.blockAST = blockAST;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return null;
    }
}
