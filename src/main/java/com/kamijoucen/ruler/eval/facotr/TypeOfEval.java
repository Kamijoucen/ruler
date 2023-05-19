package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.TypeOfNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.EvalResult;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.runtime.TypeMapping;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.StringValue;

public class TypeOfEval implements BaseEval<TypeOfNode> {
    @Override
    public EvalResult eval(TypeOfNode node, Scope scope, RuntimeContext context) {
        BaseValue value = node.getExp().eval(scope, context);
        String type = TypeMapping.find(value.getType());
        if (IOUtil.isBlank(type)) {
            throw SyntaxException.withSyntax("typeof 不支持的表达式");
        }
        return new StringValue(type);
    }
}
