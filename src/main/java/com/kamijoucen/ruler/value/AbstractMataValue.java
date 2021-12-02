package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.operation.CallOperation;
import com.kamijoucen.ruler.runtime.MataData;
import com.kamijoucen.ruler.runtime.OperationDefine;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.token.TokenType;

import java.util.List;

public abstract class AbstractMataValue implements MataValue {

    protected MataData mataData;

    protected static CallOperation callOperation = (CallOperation) OperationDefine.findOperation(TokenType.CALL);
    
    public AbstractMataValue(MataData mataData) {
        this.mataData = mataData;
    }

    @Override
    public MataData getMataData() {
        return this.mataData;
    }

    @Override
    public BaseValue getProperty(String name) {
        return mataData.get(name);
    }

    @Override
    public BaseValue invoke(RuntimeContext context, String name, List<BaseValue> param) {

        BaseValue fun = mataData.get(name);

        BaseValue[] realParam = new BaseValue[param.size() + 1];
        realParam[0] = fun;

        System.arraycopy(param.toArray(), 0, realParam, 1, param.size());
        
        return callOperation.compute(context, realParam);
    }
}
