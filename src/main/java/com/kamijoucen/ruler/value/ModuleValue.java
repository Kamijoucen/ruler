package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.runtime.MataData;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.value.constant.NullValue;

import java.util.List;

public class ModuleValue extends AbstractMataValue {

    private RulerModule module;

    public ModuleValue(RulerModule module) {
        super(null);
        this.module = module;
    }

    @Override
    public MataData getMataData() {
        throw new UnsupportedOperationException("不允许对别名进行修改操作");
    }

    @Override
    public BaseValue getProperty(String name) {
        Scope scope = module.getFileScope();
        BaseValue value = scope.find(name);
        if (value == null) {
            return NullValue.INSTANCE;
        }
        return value;
    }

    @Override
    public BaseValue invoke(RuntimeContext context, String name, List<BaseValue> param) {
        Scope scope = module.getFileScope();
        BaseValue funValue = scope.find(name);

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
