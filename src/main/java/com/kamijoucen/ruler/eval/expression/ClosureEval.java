package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ClosureDefineNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;

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
        ClosureValue closureValue = new ClosureValue(capScope, param, node.getBlock());
        if (funName != null) {
            scope.defineLocal(funName, closureValue);
        }
        return closureValue;
    }
}
