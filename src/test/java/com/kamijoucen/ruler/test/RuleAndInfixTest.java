package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.service.RulerRunner;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import com.kamijoucen.ruler.domain.parameter.SubRuleResultValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RuleAndInfixTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerRunner compileScript(String text) {
        return Ruler.compileScript(text, configuration);
    }

    // ---------- rule statement ----------

    @Test
    public void testRuleStatementSingleReturn() {
        String script = "rule 'r1' { return 42; }";
        RulerRunner runner = compileScript(script);
        RulerResult r = runner.run();
        Assert.assertEquals(1, r.size());
        Object result = r.first().getValue();
        Assert.assertTrue(result instanceof SubRuleResultValue);
        SubRuleResultValue srv = (SubRuleResultValue) result;
        Assert.assertEquals("r1", srv.getName());
        Assert.assertEquals(1, srv.getValues().size());
        Assert.assertEquals(42L, srv.getValues().get(0));
    }

    @Test
    public void testRuleStatementMultipleReturns() {
        // Due to BlockEval breaking on returnFlag, only the first return is captured.
        String script = "rule 'r1' { return 1; return 2; return 3; }";
        RulerRunner runner = compileScript(script);
        RulerResult r = runner.run();
        Assert.assertEquals(1, r.size());
        Object result = r.first().getValue();
        Assert.assertTrue(result instanceof SubRuleResultValue);
        SubRuleResultValue srv = (SubRuleResultValue) result;
        Assert.assertEquals(1, srv.getValues().size());
        Assert.assertEquals(1L, srv.getValues().get(0));
    }

    @Test
    public void testMultipleRulesOneWithReturnBreaks() {
        // Due to returnFlag not being reset by RuleStatementEval, the interpreter
        // breaks after the first rule that contains a return.
        String script = "rule 'a' { return 1; } rule 'b' { return 2; }";
        RulerRunner runner = compileScript(script);
        RulerResult r = runner.run();
        Assert.assertEquals(1, r.size());
        SubRuleResultValue srv = (SubRuleResultValue) r.first().getValue();
        Assert.assertEquals("a", srv.getName());
    }

    @Test
    public void testRuleWithNoReturn() {
        String script = "rule 'empty' { var a = 1; } return 0;";
        RulerRunner runner = compileScript(script);
        RulerResult r = runner.run();
        Assert.assertEquals(1, r.size());
        Assert.assertEquals(0L, r.first().toInteger());
    }

    // ---------- infix definition ----------

    @Test
    public void testInfixDefinition() {
        // Infix operators must be identifier-based to be parsed by the lexer/parser.
        String script = "infix fun pow(a, b) { return a - b; } return 10 pow 3;";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals(7L, r.first().toInteger());
    }

    @Test
    public void testInfixDefinitionWithClosure() {
        String script = "var base = 5; infix fun addBase(a, b) { return base + a + b; } return 1 addBase 2;";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals(8L, r.first().toInteger());
    }

    @Test
    public void testInfixDefinitionPrecedence() {
        // Custom identifier infix operators share precedence 30 with ADD/SUB.
        String script = "infix fun mul(a, b) { return a * b; } return 2 + 3 mul 4;";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals(20L, r.first().toInteger());
    }
}
