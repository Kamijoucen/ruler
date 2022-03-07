package com.kamijoucen.ruler.runtime.impl;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.common.RClassInfo;
import com.kamijoucen.ruler.eval.EvalVisitor;
import com.kamijoucen.ruler.function.*;
import com.kamijoucen.ruler.runtime.ImportCache;
import com.kamijoucen.ruler.runtime.RulerConfiguration;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.typecheck.TypeCheckVisitor;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.value.FunctionValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.HashMap;
import java.util.Map;

public class RulerConfigurationImpl implements RulerConfiguration {

    private final NodeVisitor typeCheckVisitor = new TypeCheckVisitor();
    private final NodeVisitor evalVisitor = new EvalVisitor();
    private final Scope globalScope = new Scope("root", null);
    private final ImportCache importCache = new ImportCache();
    private final Map<ValueType, RClassInfo> metaClassInfoMapping = new HashMap<ValueType, RClassInfo>();

    public RulerConfigurationImpl() {
        initDefaultFunction();
    }

    @Override
    public Scope getGlobalScope() {
        return globalScope;
    }

    @Override
    public void setGlobalFunction(RulerFunction function) {
        FunctionValue funValue = new FunctionValue(new ValueConvertFunctionProxy(function));
        this.globalScope.putLocal(funValue.getValue().getName(), funValue);
    }

    @Override
    public void removeGlobalFunction(String functionName) {
        AssertUtil.todo(null);
    }

    @Override
    public NodeVisitor getTypeCheckVisitor() {
        return typeCheckVisitor;
    }

    @Override
    public NodeVisitor getEvalVisitor() {
        return evalVisitor;
    }

    @Override
    public ImportCache getImportCache() {
        return importCache;
    }

    private void initDefaultFunction() {
        setGlobalFunction(new PrintFunction());
        setGlobalFunction(new MakeItPossibleFunction());

        RulerFunction lengthFunction = new ReturnConvertFunctionProxy(new LengthFunction());
        this.globalScope.putLocal(lengthFunction.getName(), new FunctionValue(lengthFunction));
    }
}
