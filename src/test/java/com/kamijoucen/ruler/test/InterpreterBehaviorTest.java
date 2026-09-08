package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.logic.compiler.RulerCompiler;
import com.kamijoucen.ruler.logic.eval.RulerInterpreter;
import com.kamijoucen.ruler.types.spi.CustomImportLoader;
import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.module.RulerModule;
import com.kamijoucen.ruler.types.module.RulerScript;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.ClosureValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class InterpreterBehaviorTest {

    private RulerConfiguration configuration;

    @Before
    public void init() {
        configuration = new RulerConfiguration();
        configuration.registerGlobalImportPathModule("/ruler/std/collections.txt", "listUtil");
    }

    private RulerModule compileScriptModule(String code) {
        return RulerCompiler.compileScript(new RulerScript("script", code), configuration);
    }

    private RulerModule compileStatementModule(String code) {
        return RulerCompiler.compileStatement(new RulerScript("statement", code), configuration);
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
        RulerModule interpreterModule = compileScriptModule("fun(x) { return x + 1; }");

        List<Object> result =
                RulerInterpreter.runScriptWithoutGlobalImports(interpreterModule, newRuntimeRootScope(), newRuntimeContext());

        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.get(0) instanceof ClosureValue);
    }

    @Test
    public void runScriptSingleExpressionImportsGlobalModuleByDefault() {
        RulerModule interpreterModule = compileScriptModule("listUtil.Contains(2, [1, 2, 3])");

        List<Object> result = RulerInterpreter.runScript(interpreterModule, Collections.emptyList(), newRuntimeRootScope(), configuration);

        Assert.assertEquals(Collections.singletonList(Boolean.TRUE), result);
    }

    @Test(expected = RulerRuntimeException.class)
    public void runScriptSingleExpressionCanDisableGlobalModuleImport() {
        RulerModule interpreterModule = compileScriptModule("listUtil.Contains(2, [1, 2, 3])");

        RulerInterpreter.runScriptWithoutGlobalImports(interpreterModule, newRuntimeRootScope(), newRuntimeContext());
    }

    @Test
    public void runImportModuleDropsLastExpression() {
        RulerModule interpreterModule = compileScriptModule("var a = 1; a + 2;");

        List<Object> result = RulerInterpreter.runImportModule(interpreterModule, newRuntimeRootScope(), newRuntimeContext());

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void runImportModuleDropsLastVarDefinition() {
        RulerModule interpreterModule = compileScriptModule("var answer = 42;");

        List<Object> result = RulerInterpreter.runImportModule(interpreterModule, newRuntimeRootScope(), newRuntimeContext());

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void runImportModuleKeepsExplicitReturn() {
        RulerModule interpreterModule = compileScriptModule("return 42;");

        List<Object> result = RulerInterpreter.runImportModule(interpreterModule, newRuntimeRootScope(), newRuntimeContext());

        Assert.assertEquals(Collections.singletonList(java.math.BigInteger.valueOf(42)), result);
    }

    @Test
    public void runScriptReturnsClosureWithoutConversion() {
        RulerModule interpreterModule = compileScriptModule("return fun() { return 1; };");

        List<Object> result =
                RulerInterpreter.runScriptWithoutGlobalImports(interpreterModule, newRuntimeRootScope(), newRuntimeContext());

        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.get(0) instanceof ClosureValue);
    }

    @Test
    public void runScriptTopLevelMultiReturnPreservesNullAndValues() {
        RulerModule interpreterModule = compileScriptModule("return null, 1, 'ok';");

        List<Object> result =
                RulerInterpreter.runScriptWithoutGlobalImports(interpreterModule, newRuntimeRootScope(), newRuntimeContext());

        Assert.assertEquals(3, result.size());
        Assert.assertNull(result.get(0));
        Assert.assertEquals(java.math.BigInteger.valueOf(1), result.get(1));
        Assert.assertEquals("ok", result.get(2));
    }

    @Test
    public void runScriptClearsReturnStateBetweenSharedContextRuns() {
        RuntimeContext runtimeContext = newRuntimeContext();

        RulerModule firstInterpreterModule = compileScriptModule("return 7;");
        Assert.assertEquals(Collections.singletonList(java.math.BigInteger.valueOf(7)),
                RulerInterpreter.runScriptWithoutGlobalImports(firstInterpreterModule, newRuntimeRootScope(), runtimeContext));

        RulerModule secondInterpreterModule = compileScriptModule("var a = 1; a + 2;");
        List<Object> result =
                RulerInterpreter.runScriptWithoutGlobalImports(secondInterpreterModule, newRuntimeRootScope(), runtimeContext);

        Assert.assertEquals(Collections.singletonList(java.math.BigInteger.valueOf(3)), result);
        Assert.assertFalse(runtimeContext.isReturnFlag());
        Assert.assertNull(runtimeContext.getReturnSpace());
    }

    @Test
    public void runStatementClearsStaleReturnStateBetweenSharedContextRuns() {
        Scope runScope = newShellRootScope();
        RuntimeContext runtimeContext = newRuntimeContext();

        RulerModule defineInterpreterModule = compileStatementModule("fun f() { var x = 1; return x + 1; }");
        RulerInterpreter.runStatement(defineInterpreterModule, runScope, runtimeContext);

        RulerModule returnInterpreterModule = compileStatementModule("return 42;");
        Assert.assertEquals(1, RulerInterpreter.runStatement(returnInterpreterModule, runScope, runtimeContext).size());

        RulerModule callInterpreterModule = compileStatementModule("f();");
        List<Object> result = RulerInterpreter.runStatement(callInterpreterModule, runScope, runtimeContext);

        Assert.assertEquals(Collections.singletonList(java.math.BigInteger.valueOf(2)), result);
        Assert.assertFalse(runtimeContext.isReturnFlag());
        Assert.assertNull(runtimeContext.getReturnSpace());
    }

    @Test
    public void importExecutionDoesNotLeakReturnStateToCaller() {
        configuration.getModules().registerLoader(new CustomImportLoader() {
            @Override
            public boolean match(String path) {
                return "early_return".equals(path);
            }

            @Override
            public String load(String path) {
                return "return 1; var unreachable = 2;";
            }
        });

        RulerModule interpreterModule = compileScriptModule("import 'early_return' mod; return 2;");

        List<Object> result = RulerInterpreter.runScript(interpreterModule, newRuntimeRootScope(), newRuntimeContext());

        Assert.assertEquals(Collections.singletonList(java.math.BigInteger.valueOf(2)), result);
    }

    @Test(expected = RulerRuntimeException.class)
    public void importModuleCannotReadCallerLocalVariable() {
        configuration.getModules().registerLoader(new CustomImportLoader() {
            @Override
            public boolean match(String path) {
                return "read_outer".equals(path);
            }

            @Override
            public String load(String path) {
                return "return outer;";
            }
        });

        RulerModule interpreterModule = compileScriptModule("var outer = 7; import 'read_outer' mod; return 0;");

        RulerInterpreter.runScript(interpreterModule, newRuntimeRootScope(), newRuntimeContext());
    }

    @Test(expected = RulerRuntimeException.class)
    public void importModuleDoesNotAutoImportCallerGlobalModules() {
        configuration.getModules().registerLoader(new CustomImportLoader() {
            @Override
            public boolean match(String path) {
                return "needs_global".equals(path);
            }

            @Override
            public String load(String path) {
                return "return listUtil.Contains(2, [1, 2]);";
            }
        });

        RulerModule interpreterModule = compileScriptModule("import 'needs_global' mod; return 0;");

        RulerInterpreter.runScript(interpreterModule, newRuntimeRootScope(), newRuntimeContext());
    }

    @Test
    public void repeatedImportsReuseCompiledModuleButNotRuntimeState() {
        configuration.getModules().registerLoader(new CustomImportLoader() {
            @Override
            public boolean match(String path) {
                return "counter_module".equals(path);
            }

            @Override
            public String load(String path) {
                return "var count = 0; count = count + 1;";
            }
        });

        RulerModule interpreterModule = compileScriptModule("import 'counter_module' a; import 'counter_module' b; return a.count + b.count;");

        List<Object> result = RulerInterpreter.runScript(interpreterModule, newRuntimeRootScope(), newRuntimeContext());

        Assert.assertEquals(Collections.singletonList(java.math.BigInteger.valueOf(2)), result);
    }
}
