package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.config.ParamTypePreProcess;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.eval.EvalVisitor;
import com.kamijoucen.ruler.function.*;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.typecheck.TypeCheckVisitor;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.value.FunctionValue;

public class RulerConfigurationImpl implements RulerConfiguration {

    private Scope globalScope = new Scope("root", null);
    private NodeVisitor typeCheckVisitor = new TypeCheckVisitor();
    private NodeVisitor evalVisitor = new EvalVisitor();
    private ImportCache importCache = new ImportCache();
    private ParamTypePreProcess paramTypePreProcess = new ParamTypePreProcessImpl();

    public RulerConfigurationImpl() {
        initDefaultFunction();
    }

    private void initDefaultFunction() {
        setGlobalFunction(new PrintFunction());
        setGlobalFunction(new MakeItPossibleFunction());

        RulerFunction lengthFunction = new ReturnConvertFunctionProxy(new LengthFunction());
        this.globalScope.putLocal(lengthFunction.getName(), new FunctionValue(lengthFunction));
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
    public ParamTypePreProcess getParamTypePreProcess() {
        return paramTypePreProcess;
    }

    @Override
    public ImportCache getImportCache() {
        return importCache;
    }

    @Override
    public void setGlobalModuleByPath(String path, String alias) {
    }

    public void setGlobalScope(Scope globalScope) {
        this.globalScope = globalScope;
    }

    public void setTypeCheckVisitor(NodeVisitor typeCheckVisitor) {
        this.typeCheckVisitor = typeCheckVisitor;
    }

    public void setEvalVisitor(NodeVisitor evalVisitor) {
        this.evalVisitor = evalVisitor;
    }

    public void setImportCache(ImportCache importCache) {
        this.importCache = importCache;
    }

    public void setParamTypePreProcess(ParamTypePreProcess paramTypePreProcess) {
        this.paramTypePreProcess = paramTypePreProcess;
    }
}
