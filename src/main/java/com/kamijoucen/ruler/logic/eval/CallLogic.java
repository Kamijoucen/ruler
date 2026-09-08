package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.logic.util.CollectionUtil;
import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.DefaultParamValNode;
import com.kamijoucen.ruler.types.ast.NameNode;
import com.kamijoucen.ruler.types.common.Constant;
import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenLocation;
import com.kamijoucen.ruler.types.value.ArrayValue;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.ClosureValue;
import com.kamijoucen.ruler.types.value.FunctionValue;
import com.kamijoucen.ruler.types.value.MethodValue;
import com.kamijoucen.ruler.types.value.NullValue;
import com.kamijoucen.ruler.types.value.ValueType;

import java.util.Arrays;
import java.util.List;

/** The language's calling convention, shared by expressions and native callbacks. */
public final class CallLogic {

    private CallLogic() {
    }

    public static BaseValue callValue(BaseValue callable, Scope scope, RuntimeContext context,
                                      BaseValue... params) {
        return callValue(callable, scope, context, null, params);
    }

    public static BaseValue callValue(BaseValue callable, Scope scope, RuntimeContext context,
                                      TokenLocation location, BaseValue... params) {
        BaseValue[] arguments = Arrays.copyOf(params, params.length);
        BaseValue boundSelf = null;
        if (callable instanceof MethodValue) {
            MethodValue method = (MethodValue) callable;
            boundSelf = method.getBoundSelf();
            callable = method.getTarget();
            if (callable instanceof ClosureValue) {
                arguments = new BaseValue[params.length + 1];
                arguments[0] = boundSelf;
                System.arraycopy(params, 0, arguments, 1, params.length);
            }
        }
        if (callable == null || (callable.getType() != ValueType.FUNCTION
                && callable.getType() != ValueType.CLOSURE)) {
            throw new RulerRuntimeException(callable + " is not a function", location);
        }

        context.getCallDepth().enter(context.getConfiguration().getMaxStackDepth());
        try {
            if (callable instanceof FunctionValue) {
                return (BaseValue) ((FunctionValue) callable).getValue()
                        .call(context, scope, boundSelf, (Object[]) arguments);
            }
            return callClosure((ClosureValue) callable, context, arguments);
        } finally {
            context.getCallDepth().leave();
        }
    }

    private static BaseValue callClosure(ClosureValue closure, RuntimeContext context, BaseValue[] params) {
        Scope callScope = new Scope("closure", false, closure.getDefineScope(), null);
        callScope.putLocal(Constant.FUN_ARG_LIST, new ArrayValue(Arrays.asList(params)));
        return context.withIsolatedReturn(() -> {
            List<BaseNode> defineParam = closure.getParam();
            for (int i = 0; i < defineParam.size(); i++) {
                BaseNode paramNode = defineParam.get(i);
                if (paramNode instanceof NameNode) {
                    NameNode nameNode = (NameNode) paramNode;
                    callScope.putLocal(nameNode.name.name, i >= params.length ? NullValue.INSTANCE : params[i]);
                } else if (paramNode instanceof DefaultParamValNode) {
                    DefaultParamValNode defaultParam = (DefaultParamValNode) paramNode;
                    BaseValue value = i >= params.length
                            ? EvalVisitor.evaluate(defaultParam.getExp(), callScope, context) : params[i];
                    callScope.putLocal(defaultParam.getName().name.name, value);
                } else {
                    throw new RulerRuntimeException("unsupported parameter node type", paramNode.getLocation());
                }
            }
            BaseValue blockValue = EvalVisitor.evaluate(closure.getBlock(), callScope, context);
            if (!context.isReturnFlag()) {
                return blockValue;
            }
            List<BaseValue> returnValues = context.getReturnSpace();
            if (CollectionUtil.isEmpty(returnValues)) {
                return NullValue.INSTANCE;
            }
            return returnValues.size() == 1 ? returnValues.get(0) : new ArrayValue(returnValues);
        });
    }
}
