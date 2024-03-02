package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.ast.expression.ImportScriptNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.config.*;
import com.kamijoucen.ruler.eval.EvalVisitor;
import com.kamijoucen.ruler.function.*;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.runtime.CallClosureExecutor;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.typecheck.TypeCheckVisitor;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.FunctionValue;
import com.kamijoucen.ruler.value.ModuleValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RulerConfigurationImpl implements RulerConfiguration {

    // TODO global scope会存在数据竞争问题，需要特殊处理
    private Scope globalScope = new Scope("root", false, null, null);

    private final List<ImportNode> globalImport = new ArrayList<>();

    private NodeVisitor typeCheckVisitor = new TypeCheckVisitor();

    private NodeVisitor evalVisitor = new EvalVisitor();

    private ImportCache importCache = new ImportCache();

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
        putGlobalFunction(new PrintFunction());
        putGlobalFunction(new MakeItPossibleFunction());
        putGlobalFunction(new CharAtFunction());
        putGlobalFunction(new DatetimeFunction());
        putGlobalFunction(new TimestampFunction());
        putGlobalFunction(new PanicFunction());

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
    public void putGlobalFunction(RulerFunction function) {
        FunctionValue funValue = new FunctionValue(new ValueConvertFunctionProxy(function, this));
        this.globalScope.putLocal(funValue.getValue().getName(), funValue);
    }

    @Override
    public void removeGlobalFunction(String functionName) {
        AssertUtil.TODO(null);
    }

    @Override
    public void putGlobalImportModule(String path, String alias) {
        // todo import infix
        this.globalImport.add(new ImportNode(path, alias, false, null));
    }

    @Override
    public void putGlobalImportScriptModule(String script, String alias) {
        // todo import infix
        this.globalImport.add(new ImportScriptNode(script, alias, false, null));
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
    public ImportCache getImportCache() {
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

    @Override
    public void putGlobalFunction(RulerFunction function, String moduleName) {
        // TODO 可能会出现类型转换问题
        ModuleValue moduleValue = (ModuleValue) this.globalScope.find(moduleName);
        if (moduleValue == null) {
            ModuleValue module =
                    new ModuleValue(new Scope("module:" + moduleName, false, null, null));
            moduleValue = module;
            this.globalScope.putLocal(moduleName, module);
        }
        moduleValue.getModuleScope().putLocal(function.getName(),
                new FunctionValue(new ValueConvertFunctionProxy(function, this)));
    }

    @Override
    public void removeGlobalFunction(String functionName, String moduleName) {
        ModuleValue moduleValue = (ModuleValue) this.globalScope.find(moduleName);
        if (moduleValue == null) {
            return;
        }
        moduleValue.getModuleScope().remove(functionName);
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

    public void setImportCache(ImportCache importCache) {
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
