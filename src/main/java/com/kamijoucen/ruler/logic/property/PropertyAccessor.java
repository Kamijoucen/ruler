package com.kamijoucen.ruler.logic.property;

import com.kamijoucen.ruler.component.CallClosureExecutor;
import com.kamijoucen.ruler.domain.common.Constant;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.value.*;
import com.kamijoucen.ruler.logic.util.NumberUtil;

import java.util.Objects;

/**
 * 统一属性访问器，处理所有属性/索引读写操作，包括 Proxy 拦截
 */
public final class PropertyAccessor {

    private static final String TRAP_GET = "get";
    private static final String TRAP_SET = "set";

    private PropertyAccessor() {
    }

    /**
     * 点号访问属性
     */
    public static BaseValue getProperty(BaseValue value, String name, RuntimeContext context) {
        // _target_ 保留字特殊处理：直接返回，不走 trap
        if (Constant.PROXY_TARGET.equals(name) && value instanceof ProxyValue) {
            return ((ProxyValue) value).getTarget();
        }

        return getPropertyWithProxy(value, name, context);
    }

    private static BaseValue getPropertyWithProxy(BaseValue value, String name, RuntimeContext context) {
        if (value instanceof ProxyValue) {
            ProxyValue proxy = (ProxyValue) value;
            BaseValue trap = resolveTrap(proxy.getHandler(), TRAP_GET);
            if (trap != null && trap.getType() == ValueType.CLOSURE) {
                return callClosure(context, (ClosureValue) trap,
                        proxy.getTarget(), new StringValue(name));
            }
            return getPropertyWithProxy(proxy.getTarget(), name, context);
        }
        return getPropertyDirect(value, name, context);
    }

    /**
     * 点号设置属性
     */
    public static BaseValue setProperty(BaseValue value, String name, BaseValue newValue,
                                         RuntimeContext context) {
        // 运行期保留字检查：禁止对 _xxx_ 格式赋值
        if (Constant.isReservedName(name)) {
            throw new RulerRuntimeException("cannot assign to reserved name: " + name);
        }

        // _target_ 保留字只读保护
        if (Constant.PROXY_TARGET.equals(name) && value instanceof ProxyValue) {
            throw new RulerRuntimeException("reserved property is read-only: " + Constant.PROXY_TARGET);
        }

        return setPropertyWithProxy(value, name, newValue, context);
    }

    private static BaseValue setPropertyWithProxy(BaseValue value, String name, BaseValue newValue,
                                                   RuntimeContext context) {
        if (value instanceof ProxyValue) {
            ProxyValue proxy = (ProxyValue) value;
            BaseValue trap = resolveTrap(proxy.getHandler(), TRAP_SET);
            if (trap != null && trap.getType() == ValueType.CLOSURE) {
                return callClosure(context, (ClosureValue) trap,
                        proxy.getTarget(), new StringValue(name), newValue);
            }
            return setPropertyWithProxy(proxy.getTarget(), name, newValue, context);
        }
        return setPropertyDirect(value, name, newValue, context);
    }

    /**
     * 索引访问属性
     */
    public static BaseValue getIndexProperty(BaseValue value, BaseValue key, RuntimeContext context) {
        return getIndexPropertyWithProxy(value, key, context);
    }

    private static BaseValue getIndexPropertyWithProxy(BaseValue value, BaseValue key, RuntimeContext context) {
        if (value instanceof ProxyValue) {
            ProxyValue proxy = (ProxyValue) value;
            BaseValue trap = resolveTrap(proxy.getHandler(), TRAP_GET);
            if (trap != null && trap.getType() == ValueType.CLOSURE) {
                return callClosure(context, (ClosureValue) trap, proxy.getTarget(), key);
            }
            return getIndexPropertyWithProxy(proxy.getTarget(), key, context);
        }
        return getIndexPropertyDirect(value, key, context);
    }

    /**
     * 索引设置属性
     */
    public static BaseValue setIndexProperty(BaseValue value, BaseValue key, BaseValue newValue,
                                              RuntimeContext context) {
        // 对字符串类型的 key 进行保留字检查
        if (key instanceof StringValue) {
            String keyStr = ((StringValue) key).getValue();
            if (Constant.isReservedName(keyStr)) {
                throw new RulerRuntimeException("cannot assign to reserved name: " + keyStr);
            }
            if (Constant.PROXY_TARGET.equals(keyStr) && value instanceof ProxyValue) {
                throw new RulerRuntimeException("reserved property is read-only: " + Constant.PROXY_TARGET);
            }
        }

        return setIndexPropertyWithProxy(value, key, newValue, context);
    }

    private static BaseValue setIndexPropertyWithProxy(BaseValue value, BaseValue key, BaseValue newValue,
                                                        RuntimeContext context) {
        if (value instanceof ProxyValue) {
            ProxyValue proxy = (ProxyValue) value;
            BaseValue trap = resolveTrap(proxy.getHandler(), TRAP_SET);
            if (trap != null && trap.getType() == ValueType.CLOSURE) {
                return callClosure(context, (ClosureValue) trap, proxy.getTarget(), key, newValue);
            }
            return setIndexPropertyWithProxy(proxy.getTarget(), key, newValue, context);
        }
        return setIndexPropertyDirect(value, key, newValue, context);
    }

    /**
     * 直接属性读取（无 proxy 拦截）
     */
    private static BaseValue getPropertyDirect(BaseValue value, String name, RuntimeContext context) {
        BaseValue result = null;
        if (value instanceof ModuleValue) {
            result = ((ModuleValue) value).getModuleScope().find(name);
        } else if (value.getType() == ValueType.RSON) {
            result = ((RsonValue) value).getFields().get(name);
        }
        if (result == null) {
            RClass rClass = context.getConfiguration().getRClassManager().getClassValue(value.getType());
            Objects.requireNonNull(rClass, "class not found: " + value.getType());
            result = rClass.getProperty(name);
        }
        return result == null ? NullValue.INSTANCE : result;
    }

    /**
     * 直接属性设置（无 proxy 拦截）
     */
    private static BaseValue setPropertyDirect(BaseValue value, String name, BaseValue newValue,
                                                RuntimeContext context) {
        if (value.getType() != ValueType.RSON) {
            throw new RulerRuntimeException("cannot write property to " + value.getType());
        }
        ((RsonValue) value).getFields().put(name, newValue);
        return newValue;
    }

    /**
     * 直接索引读取（无 proxy 拦截）
     */
    private static BaseValue getIndexPropertyDirect(BaseValue value, BaseValue key, RuntimeContext context) {
        if (value.getType() == ValueType.ARRAY && key.getType() == ValueType.INTEGER) {
            ArrayValue array = (ArrayValue) value;
            int index = NumberUtil.toIntIndex((IntegerValue) key);
            if (index < 0 || index >= array.getValues().size()) {
                throw new RulerRuntimeException("index out of bounds: " + index + " (size: " + array.getValues().size() + ")");
            }
            return array.getValues().get(index);
        } else if (value.getType() == ValueType.RSON && key.getType() == ValueType.STRING) {
            return getPropertyDirect(value, ((StringValue) key).getValue(), context);
        } else if (value.getType() == ValueType.STRING && key.getType() == ValueType.INTEGER) {
            StringValue str = (StringValue) value;
            int index = NumberUtil.toIntIndex((IntegerValue) key);
            if (index < 0 || index >= str.getValue().length()) {
                throw new RulerRuntimeException("index out of bounds: " + index + " (length: " + str.getValue().length() + ")");
            }
            return new StringValue(String.valueOf(str.getValue().charAt(index)));
        } else {
            throw new RulerRuntimeException("cannot read index from " + value.getType());
        }
    }

    /**
     * 直接索引设置（无 proxy 拦截）
     */
    private static BaseValue setIndexPropertyDirect(BaseValue value, BaseValue key, BaseValue newValue,
                                                     RuntimeContext context) {
        if (value.getType() == ValueType.ARRAY && key.getType() == ValueType.INTEGER) {
            ArrayValue array = (ArrayValue) value;
            int index = NumberUtil.toIntIndex((IntegerValue) key);
            if (index < 0 || index >= array.getValues().size()) {
                throw new RulerRuntimeException("index out of bounds: " + index + " (size: " + array.getValues().size() + ")");
            }
            array.getValues().set(index, newValue);
            return newValue;
        } else if (value.getType() == ValueType.RSON) {
            if (key.getType() != ValueType.STRING) {
                throw new RulerRuntimeException("invalid key type for object: " + key.getType() + ", expected string");
            }
            return setPropertyDirect(value, ((StringValue) key).getValue(), newValue, context);
        } else {
            throw new RulerRuntimeException("cannot write index to " + value.getType());
        }
    }

    /**
     * 从 handler 中解析 trap
     */
    private static BaseValue resolveTrap(RsonValue handler, String trapName) {
        BaseValue trap = handler.getFields().get(trapName);
        if (trap != null && trap.getType() == ValueType.CLOSURE) {
            return trap;
        }
        return null;
    }

    /**
     * 调用 closure，清理 returnFlag 的工作下沉到 CallClosureExecutor
     */
    private static BaseValue callClosure(RuntimeContext context, ClosureValue closure,
                                          BaseValue... params) {
        CallClosureExecutor executor = context.getConfiguration().getCallClosureExecutor();
        return executor.call(closure, null, context, params);
    }
}
