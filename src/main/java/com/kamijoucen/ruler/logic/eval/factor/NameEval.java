package com.kamijoucen.ruler.logic.eval.factor;

import com.kamijoucen.ruler.domain.ast.factor.NameNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.common.MessageType;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;

public class NameEval implements BaseEval<NameNode> {

    @Override
    public BaseValue eval(NameNode node, Scope scope, RuntimeContext context) {
        BaseValue baseValue = scope.find(node.name.name);
        if (baseValue == null) {
            String message = context.getConfiguration().getMessageManager().buildMessage(
                    MessageType.VARIABLE_NOT_DEFINED, node.name.location, node.name.name);
            throw new SyntaxException(message);
        }
        return baseValue;
    }

}
