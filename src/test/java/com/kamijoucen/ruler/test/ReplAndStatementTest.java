package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.logic.compiler.RulerCompiler;
import com.kamijoucen.ruler.logic.eval.RulerInterpreter;
import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.types.module.RulerScript;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.ClosureValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ReplAndStatementTest {

    private RulerConfiguration configuration;
    private Scope runScope;
    private RuntimeContext runtimeContext;

    @Before
    public void init() {
        configuration = new RulerConfiguration();
        runScope = new Scope("repl root", false, configuration.getGlobalScope(), null);
        runtimeContext = configuration.createDefaultRuntimeContext(null);
    }

    private List<Object> runStatement(String code) {
        com.kamijoucen.ruler.types.module.RulerModule module = RulerCompiler.compileStatement(new RulerScript("repl", code), configuration);
        return RulerInterpreter.runStatement(module, runScope, runtimeContext);
    }

    private List<Object> runScript(String code) {
        com.kamijoucen.ruler.types.module.RulerModule module = RulerCompiler.compileScript(new RulerScript("repl", code), configuration);
        return RulerInterpreter.runScriptWithoutGlobalImports(module, runScope, runtimeContext);
    }

    // ---------- compile statement basic ----------

    @Test
    public void testCompileStatementVarDefine() {
        List<Object> result = runStatement("var a = 10;");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(java.math.BigInteger.valueOf(10), result.get(0));
    }

    @Test
    public void testCompileStatementExpression() {
        List<Object> result = runStatement("1 + 2 * 3;");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(java.math.BigInteger.valueOf(7), result.get(0));
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
        Assert.assertEquals(java.math.BigInteger.valueOf(1), result.get(0));
    }

    @Test
    public void testScopePersistenceFunction() {
        List<Object> defResult = runStatement("fun add(a, b) { return a + b; }");
        Assert.assertEquals(1, defResult.size());
        Assert.assertTrue(defResult.get(0) instanceof ClosureValue);
        List<Object> result = runStatement("add(2, 3);");
        Assert.assertEquals(java.math.BigInteger.valueOf(5), result.get(0));
    }

    @Test
    public void testScopePersistenceClosure() {
        runStatement("var base = 10;");
        runStatement("var f = fun(x) { return base + x; };");
        List<Object> result = runStatement("f(5);");
        Assert.assertEquals(java.math.BigInteger.valueOf(15), result.get(0));
    }

    // ---------- multi-statement in one compile ----------

    @Test
    public void testMultiStatementInOneCompile() {
        com.kamijoucen.ruler.types.module.RulerModule module = RulerCompiler.compileStatement(new RulerScript("repl", "var a = 1; var b = 2; a + b;"), configuration);
        List<Object> result = RulerInterpreter.runStatement(module, runScope, runtimeContext);
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(java.math.BigInteger.valueOf(1), result.get(0));
        Assert.assertEquals(java.math.BigInteger.valueOf(2), result.get(1));
        Assert.assertEquals(java.math.BigInteger.valueOf(3), result.get(2));
    }

    // ---------- script implicit return ----------

    @Test
    public void testScriptImplicitReturnExpression() {
        List<Object> result = runScript("var a = 5; a + 3;");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(java.math.BigInteger.valueOf(8), result.get(0));
    }

    @Test
    public void testScriptImplicitReturnVarDefine() {
        List<Object> result = runScript("var a = 10;");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(java.math.BigInteger.valueOf(10), result.get(0));
    }

    @Test
    public void testScriptExplicitReturnStillWorks() {
        List<Object> result = runScript("return 42;");
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(java.math.BigInteger.valueOf(42), result.get(0));
    }

    @Test
    public void testScriptEmptyReturnGivesEmptyList() {
        List<Object> result = runScript("return;");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testEmptyScriptReturnsEmptyList() {
        List<Object> result = runScript("");
        Assert.assertEquals(0, result.size());
    }
}
