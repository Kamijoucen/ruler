package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.component.RulerCompiler;
import com.kamijoucen.ruler.component.RulerInterpreter;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.module.RulerScript;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.ClosureValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ReplAndStatementTest {

    private RulerConfigurationImpl configuration;
    private Scope runScope;
    private RuntimeContext runtimeContext;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
        runScope = new Scope("repl root", false, configuration.getGlobalScope(), null);
        runtimeContext = configuration.createDefaultRuntimeContext(null);
    }

    private List<Object> runStatement(String code) {
        RulerCompiler compiler = new RulerCompiler(new RulerScript("repl", code), configuration);
        com.kamijoucen.ruler.domain.module.RulerModule module = compiler.compileStatement();
        RulerInterpreter interpreter = new RulerInterpreter(module, configuration);
        interpreter.setHasImportGlobalModule(false);
        return interpreter.runStatement(runScope, runtimeContext);
    }

    // ---------- compile statement basic ----------

    @Test
    public void testCompileStatementVarDefine() {
        List<Object> result = runStatement("var a = 10;");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(10L, result.get(0));
    }

    @Test
    public void testCompileStatementExpression() {
        List<Object> result = runStatement("1 + 2 * 3;");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(7L, result.get(0));
    }

    @Test
    public void testCompileStatementReturnGivesNull() {
        // In statement/REPL mode, return sets the runtime return flag but eval returns null.
        List<Object> result = runStatement("return 42;");
        Assert.assertEquals(1, result.size());
        Assert.assertNull(result.get(0));
    }

    // ---------- scope persistence across statements ----------

    @Test
    public void testScopePersistence() {
        runStatement("var counter = 0;");
        runStatement("counter = counter + 1;");
        List<Object> result = runStatement("counter;");
        Assert.assertEquals(1L, result.get(0));
    }

    @Test
    public void testScopePersistenceFunction() {
        List<Object> defResult = runStatement("fun add(a, b) { return a + b; }");
        Assert.assertEquals(1, defResult.size());
        Assert.assertTrue(defResult.get(0) instanceof ClosureValue);
        List<Object> result = runStatement("add(2, 3);");
        Assert.assertEquals(5L, result.get(0));
    }

    @Test
    public void testScopePersistenceClosure() {
        runStatement("var base = 10;");
        runStatement("var f = fun(x) { return base + x; };");
        List<Object> result = runStatement("f(5);");
        Assert.assertEquals(15L, result.get(0));
    }

    // ---------- multi-statement in one compile ----------

    @Test
    public void testMultiStatementInOneCompile() {
        RulerCompiler compiler = new RulerCompiler(new RulerScript("repl", "var a = 1; var b = 2; a + b;"), configuration);
        com.kamijoucen.ruler.domain.module.RulerModule module = compiler.compileStatement();
        RulerInterpreter interpreter = new RulerInterpreter(module, configuration);
        interpreter.setHasImportGlobalModule(false);
        List<Object> result = interpreter.runStatement(runScope, runtimeContext);
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(1L, result.get(0));
        Assert.assertEquals(2L, result.get(1));
        Assert.assertEquals(3L, result.get(2));
    }
}
