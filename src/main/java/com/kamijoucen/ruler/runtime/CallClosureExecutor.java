package com.kamijoucen.ruler.runtime;

import java.util.Arrays;
import java.util.List;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.DefaultParamValNode;
import com.kamijoucen.ruler.ast.facotr.NameNode;
import com.kamijoucen.ruler.common.Constant;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;
import com.kamijoucen.ruler.value.constant.NullValue;

public class CallClosureExecutor {

    private final RulerConfiguration configuration;

    public CallClosureExecutor(RulerConfiguration configuration) {
        this.configuration = configuration;
    }

    public BaseValue call(BaseValue self, ClosureValue closure, Scope scope, RuntimeContext context, BaseValue... params) {

        Scope callScope = new Scope("closure", closure.getDefineScope());
        callScope.initReturnSpace();
        if (self != null) {
            callScope.putLocal(Constant.THIS_ARG, self);
        }
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
                throw new IllegalArgumentException();
            }
        }

        // call function
        closure.getBlock().eval(callScope, context);

        // get return value
        List<BaseValue> returnValues = callScope.getReturnSpace();
        if (returnValues == null || returnValues.size() == 0) {
            return NullValue.INSTANCE;
        }
        if (returnValues.size() == 1) {
            return returnValues.get(0);
        }
        return new ArrayValue(returnValues);

    }

}
