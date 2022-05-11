package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.config.ParamTypePreProcess;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.config.RuntimeBehaviorFactory;
import com.kamijoucen.ruler.eval.EvalVisitor;
import com.kamijoucen.ruler.function.*;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.typecheck.TypeCheckVisitor;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.value.FunctionValue;

import java.util.ArrayList;
import java.util.List;

public class RulerConfigurationImpl implements RulerConfiguration {

    private Scope globalScope = new Scope("root", null);
    private NodeVisitor typeCheckVisitor = new TypeCheckVisitor();
    private NodeVisitor evalVisitor = new EvalVisitor();
    private ImportCache importCache = new ImportCache();
    private ParamTypePreProcess paramTypePreProcess = new ParamTypePreProcessImpl();
    private List<ImportNode> globalImport = new ArrayList<ImportNode>();
    private RuntimeBehaviorFactory runtimeBehaviorFactory;
    private Integer maxLoopNumber = -1;
    private Integer maxStackDepth = -1;

    public RulerConfigurationImpl() {
        init();
    }

    private void init() {
        initDefaultFunction();
        runtimeBehaviorFactory = new RuntimeBehaviorFactoryImpl();
    }

    private void initDefaultFunction() {
        setGlobalFunction(new PrintFunction());
        setGlobalFunction(new MakeItPossibleFunction());
        setGlobalFunction(new CharAtFunction());
        setGlobalFunction(new DatetimeFunction());
        setGlobalFunction(new TimestampFunction());

        RulerFunction lengthFunction = new ReturnConvertFunctionProxy(new LengthFunction());
        RulerFunction charAtFunction = new ReturnConvertFunctionProxy(new CharAtFunction());
        this.globalScope.putLocal(lengthFunction.getName(), new FunctionValue(lengthFunction));
        this.globalScope.putLocal(charAtFunction.getName(), new FunctionValue(charAtFunction));
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
    public void setGlobalImportModule(String path, String alias) {
        this.globalImport.add(new ImportNode(path, alias, null));
    }

    @Override
    public List<ImportNode> getGlobalImportModules() {
        return new ArrayList<ImportNode>(globalImport);
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
    public RuntimeBehaviorFactory getRuntimeBehaviorFactory() {
        return this.runtimeBehaviorFactory;
    }

    public void setRuntimeBehaviorFactory(RuntimeBehaviorFactory runtimeBehaviorFactory) {
        this.runtimeBehaviorFactory = runtimeBehaviorFactory;
    }

    public void setMaxLoopNumber(Integer maxLoopNumber) {
        this.maxLoopNumber = maxLoopNumber;
    }

    public void setMaxStackDepth(Integer maxStackDepth) {
        this.maxStackDepth = maxStackDepth;
    }

    @Override
    public Integer getMaxLoopNumber() {
        return this.maxLoopNumber;
    }

    @Override
    public Integer getMaxStackDepth() {
        return this.maxStackDepth;
    }

    @Override
    public ImportCache getImportCache() {
        return importCache;
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
