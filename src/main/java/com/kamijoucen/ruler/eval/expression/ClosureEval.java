package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ClosureDefineNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.runtime.DefaultScope;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;

import java.util.List;

public class ClosureEval implements BaseEval<ClosureDefineNode> {

    @Override
    public BaseValue eval(ClosureDefineNode node, Environment env, RuntimeContext context,
            NodeVisitor visitor) {
        List<BaseNode> param = node.getParam();
        String funName = node.getName();
        // TODO 现在闭包只有静态捕获，通过语义分析得到需要捕获变量
        Scope capScope = new DefaultScope(null, node.getLocation());
        if (CollectionUtil.isNotEmpty(node.getStaticCaptureVar())) {
            for (BaseNode capNode : node.getStaticCaptureVar()) {
                BaseValue capValue = capNode.eval(visitor);
                capScope.define(((NameNode) capNode).name.name, capValue);
            }
        }
        ClosureValue closureValue =
                new ClosureValue(node.getName(), capScope, param, node.getBlock());
        if (funName != null) {
            env.defineLocal(funName, closureValue);
        }
        return closureValue;
    }
}
