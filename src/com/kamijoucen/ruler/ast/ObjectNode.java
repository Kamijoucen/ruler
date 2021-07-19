package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.mata.MataData;
import com.kamijoucen.ruler.mata.MataObject;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class ObjectNode implements BaseNode, MataObject {

    private MataData mataData;

    @Override
    public BaseValue eval(Scope scope) {
        return null;
    }

    @Override
    public MataData getMataData() {
        return mataData;
    }

    @Override
    public BaseValue invoke(String name) {
        return null;
    }

    @Override
    public BaseValue getProperty(String name) {
        return null;
    }
}
