package com.kamijoucen.ruler.logic.eval.expression;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.ClosureDefineNode;
import com.kamijoucen.ruler.domain.ast.factor.NameNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.logic.util.CollectionUtil;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ClosureValue;

import java.util.List;

public class ClosureEval implements BaseEval<ClosureDefineNode> {

    @Override
    public BaseValue eval(ClosureDefineNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> param = node.getParam();
        String funName = node.getName();

        Scope capScope = scope;
        if (node.isStaticCapture()) {
            capScope = new Scope(null, false, context.getConfiguration().getGlobalScope(), null);
            if (CollectionUtil.isNotEmpty(node.getStaticCaptureVar())) {
                for (BaseNode capNode : node.getStaticCaptureVar()) {
                    BaseValue capValue = capNode.eval(scope, context);
                    capScope.putLocal(((NameNode) capNode).name.name, capValue);
                }
            }
        }
        ClosureValue closureValue = new ClosureValue(node.getName(), capScope, param, node.getBlock());
        if (funName != null) {
            scope.defineLocal(funName, closureValue);
        }
        return closureValue;
    }
}
