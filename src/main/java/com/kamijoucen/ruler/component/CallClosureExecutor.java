package com.kamijoucen.ruler.component;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;

import java.util.Arrays;
import java.util.List;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.DefaultParamValNode;
import com.kamijoucen.ruler.domain.ast.factor.NameNode;
import com.kamijoucen.ruler.domain.common.Constant;
import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.logic.util.CollectionUtil;
import com.kamijoucen.ruler.domain.value.ArrayValue;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ClosureValue;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.value.NullValue;

public class CallClosureExecutor {

    private final RulerConfiguration configuration;

    public CallClosureExecutor(RulerConfiguration configuration) {
        this.configuration = configuration;
    }

    public BaseValue call(ClosureValue closure, Scope scope, RuntimeContext context, BaseValue... params) {
        // TODO
        Scope callScope = new Scope("closure", false, closure.getDefineScope(), null);
        // put args in scope
        callScope.putLocal(Constant.FUN_ARG_LIST, new ArrayValue(Arrays.asList(params)));

        List<BaseNode> defineParam = closure.getParam();
        for (int i = 0; i < defineParam.size(); i++) {
            BaseNode paramNode = defineParam.get(i);
            if (paramNode instanceof NameNode) {
                NameNode nameNode = (NameNode) defineParam.get(i);
                callScope.putLocal(nameNode.name.name, i >= params.length ? NullValue.INSTANCE : params[i]);
            } else if (paramNode instanceof DefaultParamValNode) {
                DefaultParamValNode defParamNode = (DefaultParamValNode) paramNode;
                if (i >= params.length) {
                    String paramName = defParamNode.getName().name.name;
                    BaseValue defValue = defParamNode.getExp().eval(callScope, context);
                    callScope.putLocal(paramName, defValue);
                } else {
                    callScope.putLocal(defParamNode.getName().name.name, params[i]);
                }
            } else {
                throw new RulerRuntimeException("unsupported parameter node type");
            }
        }
        // call function
        closure.getBlock().eval(callScope, context);
        // get return value
        List<BaseValue> returnSpace = context.getReturnSpace();
        if (CollectionUtil.isEmpty(returnSpace)) {
            return NullValue.INSTANCE;
        }
        if (returnSpace.size() == 1) {
            return returnSpace.get(0);
        }
        return new ArrayValue(returnSpace);

    }

}
