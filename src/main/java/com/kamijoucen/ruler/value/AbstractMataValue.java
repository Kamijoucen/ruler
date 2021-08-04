package com.kamijoucen.ruler.value;

import java.util.List;

import com.kamijoucen.ruler.operation.CallOperation;
import com.kamijoucen.ruler.runtime.MataData;
import com.kamijoucen.ruler.runtime.OperationDefine;
import com.kamijoucen.ruler.token.TokenType;

public abstract class AbstractMataValue implements MataValue {

    protected MataData mataData;

    private static CallOperation callOperation = (CallOperation) OperationDefine.findOperation(TokenType.CALL);
    
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
    public BaseValue invoke(String name, List<BaseValue> param) {

        BaseValue fun = mataData.get(name);

        ClosureValue closureValue = (ClosureValue) fun;
        closureValue.getDefineScope().putCurrentMataValue(this);

        BaseValue[] realParam = new BaseValue[param.size() + 1];
        realParam[0] = fun;

        System.arraycopy(param.toArray(), 0, realParam, 1, param.size());
        
        return callOperation.compute(realParam);
    }
}
