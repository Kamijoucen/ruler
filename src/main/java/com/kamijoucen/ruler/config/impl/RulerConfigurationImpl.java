package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.ast.expression.ImportScriptNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.config.*;
import com.kamijoucen.ruler.eval.EvalVisitor;
import com.kamijoucen.ruler.function.*;
import com.kamijoucen.ruler.runtime.CallClosureExecutor;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.typecheck.TypeCheckVisitor;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.FunctionValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RulerConfigurationImpl implements RulerConfiguration {

    // TODO global scope会存在数据竞争问题，需要特殊处理
    private Scope globalScope = new Scope("root", false, null, null);

    private final List<ImportNode> globalImport = new ArrayList<>();

    private NodeVisitor typeCheckVisitor = new TypeCheckVisitor();

    private NodeVisitor evalVisitor = new EvalVisitor();

    private ImportCacheManager importCache = new ImportCacheManager();

    private BinaryOperationFactory binaryOperationFactory = new BinaryOperationFactoryImpl();

    private ParamTypePreProcess paramTypePreProcess = new ParamTypePreProcessImpl(this);

    private RuntimeBehaviorFactory runtimeBehaviorFactory;

    private CreateRuntimeContextFactory createRuntimeContextFactory;

    private RClassManager rClassFactory;

    private Integer maxLoopNumber = -1;

    private Integer maxStackDepth = -1;

    private IntegerNumberCache integerNumberCache = new IntegerNumberCacheImpl();

    private ValueConvertManager valueConvertManager = new ValueConvertManagerImpl();

    private CallClosureExecutor callClosureExecutor = new CallClosureExecutor(this);

    private ObjectAccessControlManager objectAccessControlManager =
            new ObjectAccessControlManagerImpl();

    private SpiLoaderManager spiLoaderManager = new SpiLoaderManagerImpl();

    private MessageManager messageManager = new MessageManagerImpl();

    private ConfigModuleManager configModuleManager = new ConfigModuleManagerImpl();

    public RulerConfigurationImpl() {
        init();
    }

    private void init() {
        initEngineBehaviorFactory();
        initDefaultFunction();
        initPlugin();
    }

    private void initPlugin() {
        spiLoaderManager.load(this);
    }

    private void initEngineBehaviorFactory() {
        this.runtimeBehaviorFactory = new RuntimeBehaviorFactoryImpl();
        this.rClassFactory = new ObjectRClassManagerImpl();
        this.createRuntimeContextFactory = new CreateRuntimeContextFactoryImpl(this);
    }

    private void initDefaultFunction() {
        registerGlobalFunction(new PrintFunction());
        registerGlobalFunction(new MakeItPossibleFunction());
        registerGlobalFunction(new CharAtFunction());
        registerGlobalFunction(new DatetimeFunction());
        registerGlobalFunction(new TimestampFunction());
        registerGlobalFunction(new PanicFunction());

        RulerFunction toNumberFunction = new ToNumberFunction();
        RulerFunction toBooleanFunction = new ToBooleanFunction();
        RulerFunction proxyFunction = new ProxyFunction();

        RulerFunction charAtFunction = new ReturnConvertFunctionProxy(new CharAtFunction(), this);

        CallFunction callFunction = new CallFunction();

        this.globalScope.putLocal(toNumberFunction.getName(), new FunctionValue(toNumberFunction));
        this.globalScope.putLocal(toBooleanFunction.getName(),
                new FunctionValue(toBooleanFunction));
        this.globalScope.putLocal(charAtFunction.getName(), new FunctionValue(charAtFunction));
        this.globalScope.putLocal(proxyFunction.getName(), new FunctionValue(proxyFunction));
        this.globalScope.putLocal(callFunction.getName(), new FunctionValue(callFunction));
    }

    @Override
    public Scope getGlobalScope() {
        return globalScope;
    }

    @Override
    public void registerGlobalFunction(RulerFunction function) {
        FunctionValue funValue = new FunctionValue(new ValueConvertFunctionProxy(function, this));
        this.globalScope.putLocal(funValue.getValue().getName(), funValue);
    }

    @Override
    public void removeGlobalFunction(String functionName) {
        this.globalScope.remove(functionName);
    }

    @Override
    public void registerGlobalImportPathModule(String path, String alias) {
        this.globalImport.add(new ImportNode(path, alias, false, null));
    }

    @Override
    public void registerGlobalImportScriptModule(String script, String alias) {
        this.globalImport.add(new ImportScriptNode(script, alias, false, null));
    }

    @Override
    public List<ImportNode> getGlobalImportModules() {
        return new ArrayList<>(globalImport);
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

    @Override
    public CreateRuntimeContextFactory getCreateDefaultRuntimeContextFactory() {
        return this.createRuntimeContextFactory;
    }

    @Override
    public RClassManager getRClassManager() {
        return this.rClassFactory;
    }

    @Override
    public ObjectAccessControlManager getObjectAccessControlManager() {
        return this.objectAccessControlManager;
    }

    public void setObjectAccessControlManager(
            ObjectAccessControlManager objectAccessControlManager) {
        this.objectAccessControlManager = objectAccessControlManager;
    }

    @Override
    public MessageManager getMessageManager() {
        return messageManager;
    }

    @Override
    public ConfigModuleManager getConfigModuleManager() {
        return configModuleManager;
    }

    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    public void setRClassFactory(RClassManager metaInfoFactory) {
        this.rClassFactory = metaInfoFactory;
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
    public ImportCacheManager getImportCache() {
        return importCache;
    }

    @Override
    public BinaryOperationFactory getBinaryOperationFactory() {
        return binaryOperationFactory;
    }

    @Override
    public RuntimeContext createDefaultRuntimeContext(Map<String, BaseValue> outSpace) {
        return createRuntimeContextFactory.create(outSpace);
    }

    @Override
    public IntegerNumberCache getIntegerNumberCache() {
        return integerNumberCache;
    }

    @Override
    public ValueConvertManager getValueConvertManager() {
        return valueConvertManager;
    }

    @Override
    public CallClosureExecutor getCallClosureExecutor() {
        return callClosureExecutor;
    }

    public void setValueConvertManager(ValueConvertManager valueConvertManager) {
        this.valueConvertManager = valueConvertManager;
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

    public void setImportCache(ImportCacheManager importCache) {
        this.importCache = importCache;
    }

    public void setParamTypePreProcess(ParamTypePreProcess paramTypePreProcess) {
        this.paramTypePreProcess = paramTypePreProcess;
    }

    public CreateRuntimeContextFactory getCreateRuntimeContextFactory() {
        return createRuntimeContextFactory;
    }

    public void setCreateRuntimeContextFactory(
            CreateRuntimeContextFactory createRuntimeContextFactory) {
        this.createRuntimeContextFactory = createRuntimeContextFactory;
    }

    public void setIntegerNumberCache(IntegerNumberCache integerNumberCache) {
        this.integerNumberCache = integerNumberCache;
    }

    public void setBinaryOperationFactory(BinaryOperationFactory binaryOperationFactory) {
        this.binaryOperationFactory = binaryOperationFactory;
    }

}
