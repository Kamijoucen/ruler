package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.runtime.MataData;
import com.kamijoucen.ruler.runtime.OperationDefine;
import com.kamijoucen.ruler.token.TokenType;

public abstract class AbstractMataValue implements MataValue {

    protected MataData mataData;

    private static Operation callOperation = OperationDefine.findOperation(TokenType.CALL);
    
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
    public BaseValue invoke(String name) {

        // callOperation.compute()
        
        return null;
    }
}
