package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.runtime.Scope;

import java.util.Map;

public class ModuleValue extends RsonValue {

    private final Scope moduleScope;

    public ModuleValue(Scope moduleScope) {
        this.moduleScope = moduleScope;
    }

    @Override
    public BaseValue getField(String name) {
        return moduleScope.find(name);
    }

    @Override
    public void putField(String name, BaseValue baseValue) {
        moduleScope.putLocal(name, baseValue);
    }

    @Override
    public Map<String, BaseValue> getFields() {
        throw new UnsupportedOperationException();
    }

//    public BaseValue invoke(RuntimeContext context, String name, List<BaseValue> param) {
//        BaseValue funValue = runScope.find(name);
//        if (funValue == null) {
//            throw new IllegalArgumentException("找不到函数: " + name);
//        }
//        AssertUtil.notNull(funValue);
//        BaseValue[] realParam = new BaseValue[param.size() + 1];
//        realParam[0] = funValue;
//        System.arraycopy(param.toArray(), 0, realParam, 1, param.size());
//        return callOperation.compute(context, realParam);
//    }

}
