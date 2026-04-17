package com.kamijoucen.ruler.application.impl;
import com.kamijoucen.ruler.component.ImportCacheManager;
import com.kamijoucen.ruler.component.BinaryOperationFactoryImpl;
import com.kamijoucen.ruler.component.ParamTypePreProcessImpl;
import com.kamijoucen.ruler.component.IntegerNumberCacheImpl;
import com.kamijoucen.ruler.component.ValueConvertManagerImpl;
import com.kamijoucen.ruler.component.ObjectAccessControlManagerImpl;
import com.kamijoucen.ruler.component.SpiLoaderManagerImpl;
import com.kamijoucen.ruler.component.ConfigModuleManagerImpl;
import com.kamijoucen.ruler.component.CustomImportLoaderManagerImpl;
import com.kamijoucen.ruler.component.RuntimeBehaviorFactoryImpl;
import com.kamijoucen.ruler.component.ObjectRClassManagerImpl;
import com.kamijoucen.ruler.component.CreateRuntimeContextFactoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.kamijoucen.ruler.domain.ast.expression.ImportNode;
import com.kamijoucen.ruler.component.NodeVisitor;
import com.kamijoucen.ruler.component.BinaryOperationFactory;
import com.kamijoucen.ruler.component.ConfigModuleManager;
import com.kamijoucen.ruler.component.CreateRuntimeContextFactory;
import com.kamijoucen.ruler.component.CustomImportLoaderManager;
import com.kamijoucen.ruler.component.IntegerNumberCache;
import com.kamijoucen.ruler.component.ObjectAccessControlManager;
import com.kamijoucen.ruler.component.ParamTypePreProcess;
import com.kamijoucen.ruler.component.RClassManager;
import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.component.RuntimeBehaviorFactory;
import com.kamijoucen.ruler.component.SpiLoaderManager;
import com.kamijoucen.ruler.component.ValueConvertManager;
import com.kamijoucen.ruler.domain.ConfigModule;
import com.kamijoucen.ruler.component.option.CustomImportLoader;
import com.kamijoucen.ruler.component.option.StdImportLoader;
import com.kamijoucen.ruler.component.EvalVisitor;
import com.kamijoucen.ruler.logic.function.CallFunction;
import com.kamijoucen.ruler.logic.function.CharAtFunction;
import com.kamijoucen.ruler.logic.function.DatetimeFunction;
import com.kamijoucen.ruler.logic.function.MakeItPossibleFunction;
import com.kamijoucen.ruler.logic.function.PanicFunction;
import com.kamijoucen.ruler.logic.function.PrintFunction;
import com.kamijoucen.ruler.logic.function.ProxyFunction;
import com.kamijoucen.ruler.logic.function.ReturnConvertFunctionProxy;
import com.kamijoucen.ruler.logic.function.RulerFunction;
import com.kamijoucen.ruler.logic.function.TimestampFunction;
import com.kamijoucen.ruler.logic.function.ToBooleanFunction;
import com.kamijoucen.ruler.logic.function.ToNumberFunction;
import com.kamijoucen.ruler.logic.function.ValueConvertFunctionProxy;
import com.kamijoucen.ruler.logic.function.array.*;
import com.kamijoucen.ruler.logic.function.math.*;
import com.kamijoucen.ruler.logic.function.object.*;
import com.kamijoucen.ruler.logic.function.string.*;
import com.kamijoucen.ruler.logic.function.type.*;
import com.kamijoucen.ruler.component.CallClosureExecutor;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.component.TypeCheckVisitor;
import com.kamijoucen.ruler.domain.type.RulerType;
import com.kamijoucen.ruler.logic.util.IOUtil;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.FunctionValue;

public class RulerConfigurationImpl implements RulerConfiguration {

    // TODO global scope会存在数据竞争问题，需要特殊处理
    private Scope globalScope = new Scope("root", false, null, null);

    private final List<ImportNode> globalImport = new ArrayList<>();

    private NodeVisitor<RulerType> typeCheckVisitor = new TypeCheckVisitor();

    private NodeVisitor<BaseValue> evalVisitor = new EvalVisitor();

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

    private ConfigModuleManager configModuleManager = new ConfigModuleManagerImpl();

    private CustomImportLoaderManager customImportLoadManager = new CustomImportLoaderManagerImpl();

    public RulerConfigurationImpl() {
        init();
    }

    private void init() {
        initEngineBehaviorFactory();
        initDefaultFunction();
        initStdLoad();
        initPlugin();
    }

    private void initStdLoad() {
        CustomImportLoader stdLoad = new StdImportLoader();
        this.getCustomImportLoadManager().registerCustomImportLoader(stdLoad);
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
        registerGlobalFunction(new DatetimeFunction());
        registerGlobalFunction(new TimestampFunction());
        registerGlobalFunction(new PanicFunction());

        putGlobal(new ToNumberFunction());
        putGlobal(new ToBooleanFunction());
        putGlobal(new ProxyFunction());
        putGlobal(new CallFunction());
        putGlobal(new CharAtFunction());

        putGlobal(new AbsFunction());
        putGlobal(new MinFunction());
        putGlobal(new MaxFunction());
        putGlobal(new RoundFunction());
        putGlobal(new FloorFunction());
        putGlobal(new CeilFunction());
        putGlobal(new PowFunction());
        putGlobal(new SqrtFunction());
        putGlobal(new RandomFunction());

        putGlobal(new SubstringFunction());
        putGlobal(new com.kamijoucen.ruler.logic.function.string.IndexOfFunction());
        putGlobal(new ReplaceFunction());
        putGlobal(new SplitFunction());
        putGlobal(new TrimFunction());
        putGlobal(new UpperCaseFunction());
        putGlobal(new LowerCaseFunction());
        putGlobal(new StartsWithFunction());
        putGlobal(new EndsWithFunction());

        putGlobal(new MapFunction());
        putGlobal(new FilterFunction());
        putGlobal(new ReduceFunction());
        putGlobal(new FindFunction());
        putGlobal(new FindIndexFunction());
        putGlobal(new SliceFunction());
        putGlobal(new ConcatFunction());
        putGlobal(new JoinFunction());
        putGlobal(new ReverseFunction());
        putGlobal(new PopFunction());
        putGlobal(new ShiftFunction());
        putGlobal(new UnshiftFunction());
        putGlobal(new com.kamijoucen.ruler.logic.function.array.IndexOfFunction());
        putGlobal(new SortFunction());

        putGlobal(new KeysFunction());
        putGlobal(new ValuesFunction());
        putGlobal(new HasKeyFunction());
        putGlobal(new MergeFunction());

        putGlobal(new IsNullFunction());
        putGlobal(new IsNumberFunction());
        putGlobal(new IsStringFunction());
        putGlobal(new IsBoolFunction());
        putGlobal(new IsArrayFunction());
        putGlobal(new IsFunctionFunction());
        putGlobal(new IsDateFunction());
    }

    private void putGlobal(RulerFunction func) {
        this.globalScope.putLocal(func.getName(), new FunctionValue(func));
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
        String virtualPath = IOUtil.getVirtualPath(script, alias);

        this.getConfigModuleManager()
                .registerModule(ConfigModule.createScriptModule(virtualPath, script));
        this.globalImport.add(new ImportNode(virtualPath, alias, false, null));
    }

    @Override
    public List<ImportNode> getGlobalImportModules() {
        return new ArrayList<>(globalImport);
    }

    @Override
    public NodeVisitor<RulerType> getTypeCheckVisitor() {
        return typeCheckVisitor;
    }

    @Override
    public NodeVisitor<BaseValue> getEvalVisitor() {
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
    public ConfigModuleManager getConfigModuleManager() {
        return configModuleManager;
    }

    @Override
    public CustomImportLoaderManager getCustomImportLoadManager() {
        return this.customImportLoadManager;
    }

    public void setCustomImportLoadManager(CustomImportLoaderManager customImportLoadManager) {
        this.customImportLoadManager = customImportLoadManager;
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

    public void setTypeCheckVisitor(NodeVisitor<RulerType> typeCheckVisitor) {
        this.typeCheckVisitor = typeCheckVisitor;
    }

    public void setEvalVisitor(NodeVisitor<BaseValue> evalVisitor) {
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
