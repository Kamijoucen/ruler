package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.MessageType;
import com.kamijoucen.ruler.config.impl.MessageManagerImpl;
import com.kamijoucen.ruler.exception.RulerException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

/**
 * 名称节点求值器
 * 负责从作用域中查找变量
 *
 * @author Kamijoucen
 */
public class NameEval implements BaseEval<NameNode> {

    @Override
    public BaseValue eval(NameNode node, Scope scope, RuntimeContext context) {
        BaseValue baseValue = scope.find(node.name.name);
        if (baseValue == null) {
            // 使用MessageManager构建异常
            MessageManagerImpl messageManager = (MessageManagerImpl) context.getConfiguration().getMessageManager();
            RulerException exception = messageManager.buildException(
                    MessageType.VARIABLE_NOT_DEFINED,
                    node.name.location,
                    node.name.name
            );
            throw exception;
        }
        return baseValue;
    }

}
