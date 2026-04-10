package com.kamijoucen.ruler.logic.eval.factor;

import com.kamijoucen.ruler.domain.ast.factor.TypeOfNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.component.TypeMapping;
import com.kamijoucen.ruler.logic.util.IOUtil;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.StringValue;

public class TypeOfEval implements BaseEval<TypeOfNode> {
    @Override
    public BaseValue eval(TypeOfNode node, Scope scope, RuntimeContext context) {
        BaseValue value = node.getExp().eval(scope, context);
        String type = TypeMapping.find(value.getType());
        if (IOUtil.isBlank(type)) {
            throw SyntaxException.withSyntax("typeof unsupported expression", node.getLocation());
        }
        return new StringValue(type);
    }
}
