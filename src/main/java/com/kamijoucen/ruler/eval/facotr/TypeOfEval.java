package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.TypeOfNode;
import com.kamijoucen.ruler.eval.BaseEval;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.runtime.TypeMapping;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.StringValue;

public class TypeOfEval implements BaseEval<TypeOfNode> {
    @Override
    public BaseValue eval(TypeOfNode node, Scope scope, RuntimeContext context) {
        BaseValue value = node.getExp().eval(context, scope);
        String type = TypeMapping.lookUp(value.getType());
        if (IOUtil.isBlank(type)) {
            throw SyntaxException.withSyntax("typeof 不支持的表达式");
        }
        return new StringValue(type);
    }
}
