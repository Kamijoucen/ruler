package com.kamijoucen.ruler.eval.factor;

import com.kamijoucen.ruler.ast.factor.TypeOfNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.runtime.TypeMapping;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.StringValue;

public class TypeOfEval implements BaseEval<TypeOfNode> {
    @Override
    public BaseValue eval(TypeOfNode node, Scope scope, RuntimeContext context, NodeVisitor visitor) {
        BaseValue value = node.getExp().eval(scope, context);
        String type = TypeMapping.find(value.getType());
        if (IOUtil.isBlank(type)) {
            throw SyntaxException.withSyntax("typeof unsupported expression", node.getLocation());
        }
        return new StringValue(type);
    }
}
