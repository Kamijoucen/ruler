package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.component.RulerCompiler;
import com.kamijoucen.ruler.component.RulerInterpreter;
import com.kamijoucen.ruler.component.option.CustomImportLoader;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.module.RulerModule;
import com.kamijoucen.ruler.domain.module.RulerScript;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.ClosureValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class InterpreterBehaviorTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
        configuration.registerGlobalImportPathModule("/ruler/std/global.txt", "op");
    }

    private RulerModule compileScriptModule(String code) {
        return new RulerCompiler(new RulerScript("script", code), configuration)
                .compileScript();
    }

    private RulerModule compileStatementModule(String code) {
        return new RulerCompiler(new RulerScript("statement", code), configuration)
                .compileStatement();
    }

    private Scope newRuntimeRootScope() {
        return new Scope("runtime root", true, configuration.getGlobalScope(), null);
    }

    private Scope newShellRootScope() {
        return new Scope("shell root", false, configuration.getGlobalScope(), null);
    }

    private RuntimeContext newRuntimeContext() {
        return configuration.createDefaultRuntimeContext(null);
    }

    @Test
    public void runScriptSingleExpressionReturnsClosureWithoutConversion() {
        RulerInterpreter interpreter =
                new RulerInterpreter(compileScriptModule("fun(x) { return x + 1; }"), configuration);
        interpreter.setHasImportGlobalModule(false);

        List<Object> result = interpreter.runScript(Collections.emptyList(), newRuntimeRootScope());

        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.get(0) instanceof ClosureValue);
    }

    @Test
    public void runScriptSingleExpressionImportsGlobalModuleByDefault() {
        RulerInterpreter interpreter =
                new RulerInterpreter(compileScriptModule("op.Add(1, 2, 3)"), configuration);

        List<Object> result = interpreter.runScript(Collections.emptyList(), newRuntimeRootScope());

        Assert.assertEquals(Collections.singletonList(6L), result);
    }

    @Test(expected = RulerRuntimeException.class)
    public void runScriptSingleExpressionCanDisableGlobalModuleImport() {
        RulerInterpreter interpreter =
                new RulerInterpreter(compileScriptModule("op.Add(1, 2)"), configuration);
        interpreter.setHasImportGlobalModule(false);

        interpreter.runScript(Collections.emptyList(), newRuntimeRootScope());
    }

    @Test
    public void runScriptDisablingImplicitReturnDropsLastExpression() {
        RulerInterpreter interpreter =
                new RulerInterpreter(compileScriptModule("var a = 1; a + 2;"), configuration);
        interpreter.setHasImportGlobalModule(false);
        interpreter.setImplicitReturn(false);

        List<Object> result = interpreter.runScript(newRuntimeRootScope(), newRuntimeContext());

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void runScriptDisablingImplicitReturnDropsLastVarDefinition() {
        RulerInterpreter interpreter =
                new RulerInterpreter(compileScriptModule("var answer = 42;"), configuration);
        interpreter.setHasImportGlobalModule(false);
        interpreter.setImplicitReturn(false);

        List<Object> result = interpreter.runScript(newRuntimeRootScope(), newRuntimeContext());

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void runScriptExplicitReturnStillWorksWhenImplicitReturnDisabled() {
        RulerInterpreter interpreter =
                new RulerInterpreter(compileScriptModule("return 42;"), configuration);
        interpreter.setHasImportGlobalModule(false);
        interpreter.setImplicitReturn(false);

        List<Object> result = interpreter.runScript(newRuntimeRootScope(), newRuntimeContext());

        Assert.assertEquals(Collections.singletonList(42L), result);
    }

    @Test
    public void runScriptReturnsClosureWithoutConversion() {
        RulerInterpreter interpreter =
                new RulerInterpreter(compileScriptModule("return fun() { return 1; };"), configuration);
        interpreter.setHasImportGlobalModule(false);

        List<Object> result = interpreter.runScript(newRuntimeRootScope(), newRuntimeContext());

        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.get(0) instanceof ClosureValue);
    }

    @Test
    public void runScriptTopLevelMultiReturnPreservesNullAndValues() {
        RulerInterpreter interpreter =
                new RulerInterpreter(compileScriptModule("return null, 1, 'ok';"), configuration);
        interpreter.setHasImportGlobalModule(false);

        List<Object> result = interpreter.runScript(newRuntimeRootScope(), newRuntimeContext());

        Assert.assertEquals(3, result.size());
        Assert.assertNull(result.get(0));
        Assert.assertEquals(1L, result.get(1));
        Assert.assertEquals("ok", result.get(2));
    }

    @Test
    public void runScriptClearsReturnStateBetweenSharedContextRuns() {
        RuntimeContext runtimeContext = newRuntimeContext();

        RulerInterpreter firstInterpreter =
                new RulerInterpreter(compileScriptModule("return 7;"), configuration);
        firstInterpreter.setHasImportGlobalModule(false);
        Assert.assertEquals(Collections.singletonList(7L),
                firstInterpreter.runScript(newRuntimeRootScope(), runtimeContext));

        RulerInterpreter secondInterpreter =
                new RulerInterpreter(compileScriptModule("var a = 1; a + 2;"), configuration);
        secondInterpreter.setHasImportGlobalModule(false);
        List<Object> result = secondInterpreter.runScript(newRuntimeRootScope(), runtimeContext);

        Assert.assertEquals(Collections.singletonList(3L), result);
        Assert.assertFalse(runtimeContext.isReturnFlag());
        Assert.assertNull(runtimeContext.getReturnSpace());
    }

    @Test
    public void runStatementClearsStaleReturnStateBetweenSharedContextRuns() {
        Scope runScope = newShellRootScope();
        RuntimeContext runtimeContext = newRuntimeContext();

        RulerInterpreter defineInterpreter =
                new RulerInterpreter(compileStatementModule("fun f() { var x = 1; return x + 1; }"), configuration);
        defineInterpreter.setHasImportGlobalModule(false);
        defineInterpreter.runStatement(runScope, runtimeContext);

        RulerInterpreter returnInterpreter =
                new RulerInterpreter(compileStatementModule("return 42;"), configuration);
        returnInterpreter.setHasImportGlobalModule(false);
        Assert.assertEquals(1, returnInterpreter.runStatement(runScope, runtimeContext).size());

        RulerInterpreter callInterpreter =
                new RulerInterpreter(compileStatementModule("f();"), configuration);
        callInterpreter.setHasImportGlobalModule(false);
        List<Object> result = callInterpreter.runStatement(runScope, runtimeContext);

        Assert.assertEquals(Collections.singletonList(2L), result);
        Assert.assertFalse(runtimeContext.isReturnFlag());
        Assert.assertNull(runtimeContext.getReturnSpace());
    }

    @Test
    public void importExecutionDoesNotLeakReturnStateToCaller() {
        configuration.getCustomImportLoadManager().registerCustomImportLoader(new CustomImportLoader() {
            @Override
            public boolean match(String path) {
                return "early_return".equals(path);
            }

            @Override
            public String load(String path) {
                return "return 1; var unreachable = 2;";
            }
        });

        RulerInterpreter interpreter =
                new RulerInterpreter(compileScriptModule("import 'early_return' mod; return 2;"), configuration);

        List<Object> result = interpreter.runScript(newRuntimeRootScope(), newRuntimeContext());

        Assert.assertEquals(Collections.singletonList(2L), result);
    }

    @Test(expected = RulerRuntimeException.class)
    public void importModuleCannotReadCallerLocalVariable() {
        configuration.getCustomImportLoadManager().registerCustomImportLoader(new CustomImportLoader() {
            @Override
            public boolean match(String path) {
                return "read_outer".equals(path);
            }

            @Override
            public String load(String path) {
                return "return outer;";
            }
        });

        RulerInterpreter interpreter = new RulerInterpreter(
                compileScriptModule("var outer = 7; import 'read_outer' mod; return 0;"),
                configuration);

        interpreter.runScript(newRuntimeRootScope(), newRuntimeContext());
    }

    @Test(expected = RulerRuntimeException.class)
    public void importModuleDoesNotAutoImportCallerGlobalModules() {
        configuration.getCustomImportLoadManager().registerCustomImportLoader(new CustomImportLoader() {
            @Override
            public boolean match(String path) {
                return "needs_global".equals(path);
            }

            @Override
            public String load(String path) {
                return "return op.Add(1, 2);";
            }
        });

        RulerInterpreter interpreter =
                new RulerInterpreter(compileScriptModule("import 'needs_global' mod; return 0;"), configuration);

        interpreter.runScript(newRuntimeRootScope(), newRuntimeContext());
    }

    @Test
    public void repeatedImportsReuseCompiledModuleButNotRuntimeState() {
        configuration.getCustomImportLoadManager().registerCustomImportLoader(new CustomImportLoader() {
            @Override
            public boolean match(String path) {
                return "counter_module".equals(path);
            }

            @Override
            public String load(String path) {
                return "var count = 0; count = count + 1;";
            }
        });

        RulerInterpreter interpreter = new RulerInterpreter(
                compileScriptModule("import 'counter_module' a; import 'counter_module' b; return a.count + b.count;"),
                configuration);

        List<Object> result = interpreter.runScript(newRuntimeRootScope(), newRuntimeContext());

        Assert.assertEquals(Collections.singletonList(2L), result);
    }
}
