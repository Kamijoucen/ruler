package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.NameNode;
import com.kamijoucen.ruler.eval.BaseEval;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class NameEval implements BaseEval<NameNode> {

    @Override
    public BaseValue eval(NameNode node, Scope scope, RuntimeContext context) {
        BaseValue baseValue = scope.find(node.name.name);
        if (baseValue == null) {
            throw SyntaxException.withSyntax("'" + node.name.name + "'未定义");
        }
        return baseValue;
    }

}