package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.operation.CallOperation;
import com.kamijoucen.ruler.common.OperationDefine;
import com.kamijoucen.ruler.common.RMetaInfo;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.token.TokenType;

import java.util.List;

public abstract class AbstractRClassValue implements MetaValue {

    protected RMetaInfo classInfo;
    protected static CallOperation callOperation = (CallOperation) OperationDefine.findOperation(TokenType.CALL);

    public AbstractRClassValue(RMetaInfo classInfo) {
        this.classInfo = classInfo;
        this.classInfo.setSource(this);
    }

    @Override
    public RMetaInfo getClassInfo() {
        return this.classInfo;
    }

    @Override
    public BaseValue getProperty(String name) {
        return classInfo.get(name);
    }

    @Override
    public BaseValue invoke(RuntimeContext context, String name, List<BaseValue> param) {
        BaseValue fun = classInfo.get(name);
        if (fun == null) {
            throw SyntaxException.withSyntax("function not found: " + name);
        }
        BaseValue[] realParam = new BaseValue[param.size() + 1];
        realParam[0] = fun;
        System.arraycopy(param.toArray(), 0, realParam, 1, param.size());
        return callOperation.compute(context, realParam);
    }
}
