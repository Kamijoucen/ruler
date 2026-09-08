package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.TypeOfNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.logic.util.TypeMapping;
import com.kamijoucen.ruler.logic.util.IOUtil;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.StringValue;

public class TypeOfEval implements BaseEval<TypeOfNode> {
    @Override
    public BaseValue eval(TypeOfNode node, Scope scope, RuntimeContext context) {
        BaseValue value = EvalVisitor.evaluate(node.getExp(), scope, context);
        String type = TypeMapping.find(value.getType());
        if (IOUtil.isBlank(type)) {
            throw new RulerRuntimeException("unsupported expression for typeof", node.getLocation());
        }
        return new StringValue(type);
    }
}
