package com.kamijoucen.ruler.value;

import com.kamijoucen.ruler.exception.TypeException;
import com.kamijoucen.ruler.runtime.Scope;

import java.util.Map;

/**
 * 模块值
 * 表示一个已导入的模块
 *
 * @author Kamijoucen
 */
public class ModuleValue extends RsonValue {

    private final Scope moduleScope;

    public ModuleValue(Scope moduleScope) {
        super(null);
        this.moduleScope = moduleScope;
    }

    @Override
    public Map<String, BaseValue> getFields() {
        throw TypeException.unsupportedOperation("获取字段", this.getType(), null);
    }

    public Scope getModuleScope() {
        return moduleScope;
    }

}
