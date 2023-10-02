package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

public class ProxyFunction implements RulerFunction {

    @Override
    public String getName() {
        return "Proxy";
    }

    @Override
    public Object call(RuntimeContext context, BaseValue self, Object... param) {

        
        throw new UnsupportedOperationException("Unimplemented method 'call'");
    }
    
}
