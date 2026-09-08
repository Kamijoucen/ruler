package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.NameNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;

public class NameEval implements BaseEval<NameNode> {

    @Override
    public BaseValue eval(NameNode node, Scope scope, RuntimeContext context) {
        BaseValue baseValue = scope.find(node.name.name);
        if (baseValue == null) {
            throw new SyntaxException("variable not defined '" + node.name.name + "'",
                    node.name.location);
        }
        return baseValue;
    }

}
