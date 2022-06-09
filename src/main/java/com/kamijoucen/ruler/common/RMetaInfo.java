package com.kamijoucen.ruler.common;

import com.kamijoucen.ruler.runtime.metafun.AbstractMetaFun;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.FunctionValue;

import java.util.HashMap;
import java.util.Map;

public class RMetaInfo {

    private BaseValue source;
    private final Map<String, BaseValue> properties;

    public RMetaInfo() {
        this.properties = new HashMap<String, BaseValue>();
    }

    public void initMetaFun(AbstractMetaFun fun) {
        fun.setMetaInfo(this);
        this.put(fun.getName(), new FunctionValue(fun));
    }

    public void put(String name, BaseValue value) {
        this.properties.put(name, value);
    }

    public BaseValue get(String name) {
        return this.properties.get(name);
    }

    public BaseValue getSource() {
        return source;
    }

    public void setSource(BaseValue source) {
        this.source = source;
    }
}
