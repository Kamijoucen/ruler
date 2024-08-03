package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.MessageType;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

public class NameEval implements BaseEval<NameNode> {

    @Override
    public BaseValue eval(NameNode node, Environment env, RuntimeContext context, NodeVisitor visitor) {
        BaseValue baseValue = env.find(node.name.name);
        if (baseValue == null) {
            String message = context.getConfiguration().getMessageManager().buildMessage(
                    MessageType.VARIABLE_NOT_DEFINED, node.name.location, node.name.name);
            throw new SyntaxException(message);
        }
        return baseValue;
    }

}
