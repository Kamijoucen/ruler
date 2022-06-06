package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.common.RMateInfo;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.value.constant.NullValue;

import java.util.List;

public class ModuleValue extends AbstractRClassValue {

    private RulerModule module;
    private Scope runScope;

    public ModuleValue(RulerModule module, Scope scope) {
        super(null);
        this.module = module;
        this.runScope = scope;
    }

    @Override
    public RMateInfo getClassInfo() {
        throw new UnsupportedOperationException("不允许对别名进行修改操作");
    }

    @Override
    public BaseValue getProperty(String name) {
        BaseValue value = runScope.find(name);
        if (value == null) {
            return NullValue.INSTANCE;
        }
        return value;
    }

    @Override
    public BaseValue invoke(RuntimeContext context, String name, List<BaseValue> param) {
        BaseValue funValue = runScope.find(name);
        if (funValue == null) {
            throw new IllegalArgumentException("找不到函数: " + name);
        }
        AssertUtil.notNull(funValue);
        BaseValue[] realParam = new BaseValue[param.size() + 1];
        realParam[0] = funValue;
        System.arraycopy(param.toArray(), 0, realParam, 1, param.size());
        return callOperation.compute(context, realParam);
    }

    @Override
    public ValueType getType() {
        return ValueType.RSON;
    }
}
