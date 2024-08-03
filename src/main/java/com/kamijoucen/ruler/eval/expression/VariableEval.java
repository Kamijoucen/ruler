package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.expression.VariableDefineNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.NullValue;

public class VariableEval implements BaseEval<VariableDefineNode> {
    @Override
    public BaseValue eval(VariableDefineNode node, Environment env, RuntimeContext context,
            NodeVisitor visitor) {
        NameNode lhs = (NameNode) node.getLhs();
        if (node.getRhs() != null) {
            BaseValue rValue = node.getRhs().eval(visitor);
            env.defineLocal(lhs.name.name, rValue);
            return rValue;
        } else {
            env.defineLocal(lhs.name.name, NullValue.INSTANCE);
            return NullValue.INSTANCE;
        }
    }
}
